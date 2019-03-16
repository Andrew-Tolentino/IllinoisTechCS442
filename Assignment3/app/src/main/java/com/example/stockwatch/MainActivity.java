package com.example.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener{

    // Declare SwipeRefreshLayout object
    private SwipeRefreshLayout s;

    // Declare RecyclerView object
    private RecyclerView recyclerView;

    // List of Stocks. This will be linked to the adapter
    private List<Stock> stocks = new ArrayList<>();

    // ArrayList of stocks from the database. Each element will contain the stock's symbol and company name only
    private ArrayList<String[]> databaseStocks;

    // Create stock adapter
    private StockAdapter stockAdapter = new StockAdapter(stocks, this);

    // Declare DatabaseHandler object
    private DatabaseHandler databaseHandler;

    // Declare HashMap that will be filled with all the stock symbols and stock names from AsyncNameDownloader
    private HashMap<String, String> stockHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link RecyclerView r
        recyclerView = findViewById(R.id.recycler);

        // Since the content does not change the layout size
        recyclerView.hasFixedSize();

        // Assign LayoutManager to recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the adapter for the recyclerView
        recyclerView.setAdapter(stockAdapter);

        // Separate each item in the recyclerView
        recyclerView.addItemDecoration(new DividerItemDecoration(30));

        // Link SwipeRefreshLayout object to SwipeRefreshLayout in XML
        s = findViewById(R.id.swiper);

        // Attach SwipeRefreshLayout listener to the SwipeRefreshLayout object
        s.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // If the user is connected
                if (checkConnection()) {
                    // Update all stocks from database
                    refresh();
                }

                // User is not connected. Show a simple Dialog
                else {
                    AlertDialog.Builder builderNotConnected = new AlertDialog.Builder(MainActivity.this);

                    // Set title
                    builderNotConnected.setTitle("No Network Connection");

                    // Set message
                    builderNotConnected.setMessage("Stocks Cannot Be Updated Without A Network Connection");

                    // Show Dialog
                    AlertDialog dialog = builderNotConnected.create();
                    dialog.show();

                }

                // Stop swipe spinner
                s.setRefreshing(false);
            }
        });

        // Initialize the databaseHandler
        databaseHandler = new DatabaseHandler(this);

        // Get the stocks from the the database
        databaseStocks = databaseHandler.getStocks();

        // We have access to the user's network status
        if (checkConnection()) {
            // Set connection status to true

            // With each stock from the database, use AsyncStockDownloader to add them to the stocks list and display them
            for (int i = 0; i < databaseStocks.size(); i++) {
                // Make call to get stock information. Passing in stock's symbol and company name as arguments
                new AsyncStockDownloader(this, -1).execute(new String[] {databaseStocks.get(i)[0], databaseStocks.get(i)[1]});
            }

            // Execute AsyncNameLoader to get all the stock symbols and company names
            new AsyncNameDownloader(this).execute();
        }

        // User has no connection. Display all stocks from database with price, price change, and change percentage as 0
        else {
            // Iterate through the stocks from the database and append those stocks to the stocks list with a price, price change, and change percentage as 0
            for (int i = 0; i < databaseStocks.size(); i++) {
                // Create stock
                Stock newStock = new Stock(databaseStocks.get(i)[0], databaseStocks.get(i)[1], 0,0,0);

                // Add stock to stocks list
                stocks.add(newStock);

                // Notify adapter
                stockAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void onResume() {
        // Print what is inside the database
        databaseHandler.dumpDbToLog();

        super.onResume();
    }

    // When the app destroys close the databasehandler
    @Override
    protected void onDestroy() {
        databaseHandler.shutDown();
        super.onDestroy();
    }

    // Inflate the menu for the Main Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create a MenuInflater instance for Main Actvity
        MenuInflater inflater = getMenuInflater();

        // Inflate the xml file of the main_menu
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // Function that will handle callback when menu items are selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // If the add stock menu item is pressed
        if (item.getItemId() == R.id.add_stock) {
            // There is a connection
            if (checkConnection()) {
                /* Create Dialog with a layout */

                // Instantiate a LayoutInflator object which will be used to inflate a View object
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View view = layoutInflater.inflate(R.layout.dialog, null);

                // Instantiate AlertDialog.Builder object to create a dialog with a layout
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // Set the view of the builder to the dialog layout in the view object
                builder.setView(view);

                // Set the title and message of the dialog
                builder.setTitle("Stock Selection");
                builder.setMessage("Please enter a Stock Symbol:");

                // Positive button if user wishes to search for the particular stock
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // input will contain what stock the use searched for
                        EditText input = view.findViewById(R.id.stock_input);

                        // Contains what the user searched
                        String inputStockSymbol = input.getText().toString();

                        // Search for stocks based on what user searched
                        searchStock(inputStockSymbol);

                    }
                });

                // Negative button if the user wishes to cancel the search
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // Display Dialog with layout
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            // There is no connection
            else {
                // Display a simple Dialog telling the user there is no network connection

                AlertDialog.Builder builderNoConnection = new AlertDialog.Builder(this);

                // Set title
                builderNoConnection.setTitle("No Network Connection");

                // Set Message
                builderNoConnection.setMessage("Stocks Cannot Be Added Without A Network Connection");

                // Show Dialog
                AlertDialog dialog = builderNoConnection.create();
                dialog.show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // The user is connected
        if (checkConnection()) {
            // Get position of the view holder
            int pos = recyclerView.getChildAdapterPosition(v);

            // Get the stock's symbol name
            String stockSymbol = stocks.get(pos).getStockSymbol();

            // Set up the URL based on the stockSymbol
            String URL = "https://www.marketwatch.com/investing/stock/" + stockSymbol;

            // Initialize the implied Intent that will open up the URL through the phone's web browser
            Intent i = new Intent(Intent.ACTION_VIEW);

            // Set the data the intent is operating on by setting up the URL
            i.setData(Uri.parse(URL));

            // Start activity and go to the stock's website
            startActivity(i);
        }

        // User is not connected. Show a Dialog
        else {
            AlertDialog.Builder builderIsNotConnected = new AlertDialog.Builder(this);

            // Set title
            builderIsNotConnected.setTitle("No Network Connection");

            // Set message
            builderIsNotConnected.setMessage("Cannot Get More Information About Stocks Without A Network Connection");

            // Show Dialog
            AlertDialog dialog = builderIsNotConnected.create();
            dialog.show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        // Display dialog box to prompt user if they would like to delete stock

        // Create a AlertDialog Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get position of item in recycler view
        final int pos = recyclerView.getChildLayoutPosition(v);

        // Set option to delete stock
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove stock from database
                databaseHandler.deleteStock(stocks.get(pos).getStockSymbol());

                // Remove item from the list in Main Activity containing all the stocks
                stocks.remove(pos);

                // Notify adapter linked to that list
                stockAdapter.notifyDataSetChanged();
            }
        });


        // Set cancel option
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
            }
        });

        // Set dialog title
        builder.setTitle("Delete Stock");

        // Set body message
        builder.setMessage("Delete Stock Symbol " + stocks.get(pos).getStockSymbol());

        // Show dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    // Set the stockHashMap. AsyncNameDownloader will use this function
    public void setHashMap(HashMap<String, String> hashMap) {
        // Initialize the HashMap
        this.stockHashMap = hashMap;
    }

    public void searchStock(String input) {
        // If the input is not an empty String
        if (!input.isEmpty()) {
            // Check for duplicates
            for (int i = 0; i < stocks.size(); i++) {
                // If there is a duplicate, alert user and return
                if (stocks.get(i).getStockSymbol().equals(input)) {
                    // Create builder
                    AlertDialog.Builder builderDuplicate = new AlertDialog.Builder(this);

                    // Set title
                    builderDuplicate.setTitle("Duplicate Stock");

                    // Set Message
                    builderDuplicate.setMessage("Stock Symbol " + input + " is already displayed");

                    // Create dialog and display
                    AlertDialog dialogDuplicate = builderDuplicate.create();
                    dialogDuplicate.show();
                    return;
                }
            }


            /* Check if inputStockSymbol is a substring of a stock's symbol or company name in the HashMap and display these stocks in a dialog list */

            // List containing the stocks to display
            final List<String> listItems = new ArrayList<String>();

            // Iterate through the HashMap
            for (Map.Entry<String, String> entry: stockHashMap.entrySet()) {
                // Stock symbol
                String key = entry.getKey();

                // Stock's company name
                String value = entry.getValue();

                // Check if inputStockSymbol is a substring of the stock's symbol or company name
                if (key.contains(input) || value.contains(input)) {

                    // Add key and value in the list
                    String displayFormat = key + " - " + value;
                    listItems.add(key);
                }

            }

            // If stocks were found matching the user's input
            if (listItems.size() > 0) {
                // Array of Strings to display to the user for the dialog list
                final CharSequence[] displayList = new CharSequence[listItems.size()];

                // Set up the String format for each item in the list for the dialog list
                for (int i = 0; i < listItems.size(); i++) {
                    displayList[i] = listItems.get(i) + " - " + stockHashMap.get(listItems.get(i));
                }

                // builder for the dialog list
                AlertDialog.Builder builderList = new AlertDialog.Builder(this);

                // Set dialog title
                builderList.setTitle("Make a selection");

                // Set the items to display on the dialog each with onClick listeners
                builderList.setItems(displayList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which parameter contains the index of the item from displayList

                        // Call Asynchronous Task to get the stock information and passing in the stock's symbol and the stock's company name as arguments
                        new AsyncStockDownloader(MainActivity.this, -1).execute(new String[]{listItems.get(which), stockHashMap.get(listItems.get(which))});

                        // Add stock symbol and company name to database
                        databaseHandler.addStock(listItems.get(which), stockHashMap.get(listItems.get(which)));
                    }
                });

                // Set the negative button if the user declines
                builderList.setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // Display Dialog
                AlertDialog dialogList = builderList.create();
                dialogList.show();
            }

            // No stocks found matching the user's input
            else {

                // Create a simple Dialog showing the stock symbol was not found
                AlertDialog.Builder builderNotFound = new AlertDialog.Builder(this);

                // Set title and message
                builderNotFound.setTitle("Symbol Not Found: " + input);
                builderNotFound.setMessage("Data for stock symbol");

                // Create Dialog and show it
                AlertDialog dialogNotFound = builderNotFound.create();
                dialogNotFound.show();


            }

        }
    }

    // Function to add a new stock to stocks. Used in AsyncStockDownloader
    public void addStock(Stock newStock) {
        // Add the new stock
        stocks.add(newStock);

        // Notify adapter that a new item has been added
        stockAdapter.notifyDataSetChanged();
    }

    // Function to update a stock. Used in AsyncStockDownloader
    public void updateStock(Stock stock, int index) {
        // To update, remove the original stock at the index and replace it with one that has been recently updated at the index
        stocks.remove(index);
        stocks.add(index, stock);

        stockAdapter.notifyDataSetChanged();
    }

    // Callback for the refresher. Will update all stocks in Database here
    public void refresh() {
        // TODO: Implement swiper callback

        // Iterate through all stocks and update them
        for (int i = 0; i < stocks.size(); i++) {
            new AsyncStockDownloader(this, i).execute(new String[] {stocks.get(i).getStockSymbol(), stocks.get(i).getCompanyName()});
        }

    }

    // Function used to check the status of the user's connection
    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Access to network status
        if (connectivityManager != null) {

            // Create a NetworkInfo object to get the status of the network
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            // User has connection
            if (networkInfo != null  && networkInfo.isConnected()) {
                return true;
            }
        }

        return false;
    }
}
