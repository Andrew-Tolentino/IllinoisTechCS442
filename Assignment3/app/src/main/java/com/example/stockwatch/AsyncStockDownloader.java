package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncStockDownloader extends AsyncTask<String, Double, String> {

    // URL to fetch information of the individual stock. Need to append "SYMBOL/quote?displayPercent=true"
    private static final String STOCK_URL = "https://api.iextrading.com/1.0/stock/";

    // Tag for logging
    private static final String TAG = "AsyncStockDownloader";

    // Reference variable to Main Activity
    private MainActivity mainActivity;

    // The stock's symbol
    private String stockSymbol;

    // The company name of the stock
    private String companyName;

    // Index of the stock if there is any in the stocks list. If pos = -1 a new stock will be added to stocks
    private int pos;

    // Constructor
    public AsyncStockDownloader(MainActivity ma, int i) {
        this.mainActivity = ma;
        this.pos = i;
    }

    @Override
    protected String doInBackground(String... strings) {

        // Initialize the stock's symbol
        this.stockSymbol = strings[0];

        // Initialize the stock's company name
        this.companyName = strings[1];

        // The input here will be the Stock symbol
        String stockSymbol = strings[0];

        // Append to STOCK_URL with the stockSymbol query format
        String DATA_URL =  STOCK_URL + stockSymbol + "/quote?displayPercent=true";

        // Set the uri object to represent the appropriate uri used to URLs
        Uri uri = Uri.parse(DATA_URL);

        // Set uri to a String that will then be used to instantiate a URL object
        String urlToUse = uri.toString();

        // We use a StringBuilder to efficiently append Strings without being to slow
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            // Instantiate a HttpURLConnection object that will represent the communication link between the app and the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Send the GET Request to get JSON object the URL contains
            conn.setRequestMethod("GET");

            /* Parse through the JSON and convert it into a String */

            // Get the raw bytes of the output
            InputStream is = conn.getInputStream();

            // Decode the bytes into the default charset and user a buffer to go through them efficiently
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            // line will be used to get each line of the reader. Each line of the reader has a \n new line at the end
            String line;

            // Go through each line in the reader
            while ((line = reader.readLine()) != null) {
                // add line to sb
                sb.append(line).append('\n');
            }

            return sb.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        /* Parse through the String JSON Object, create a Stock object, and add it to the stocks in Main Activity */

        try {
            JSONObject jsonObject = new JSONObject(s);

            // Get the stock's latest price
            Double price = Double.valueOf(jsonObject.getDouble("latestPrice"));

            // Get the stock's price change
            Double priceChange = Double.valueOf(jsonObject.getDouble("change"));

            // Get the stock's change percentage
            Double changePercentage = Double.valueOf(jsonObject.getDouble("changePercent"));

            // Create the new Stock object
            Stock newStock = new Stock(this.stockSymbol, this.companyName, price, priceChange, changePercentage);

            // If newStock is already in the stocks list
            if (this.pos > -1) {
                // Replace the stock at pos in the stocks list
                this.mainActivity.updateStock(newStock, this.pos);
            }

            // newStock is not in the stocks list
            else {
                // Append newStock to stocks in Main Activity
                this.mainActivity.addStock(newStock);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
