package com.example.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    // TAG for logging
    private static final String TAG = "DatabaseHandler";

    // Database version
    private static final int DB_VERSION = 1;

    // Database name
    private static final String DB_NAME = "StockWatchDB";

    // Database Table Name
    private static final String STOCK_TABLE = "StockTable";

    // Declare a SQLiteDatabase variable that will be used to interact with the Database
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        // Create database. When this is called and no database is made yet it will first run onCreate(SQLiteDatabase db) for the first time. This usually only happens once
        db = getWritableDatabase();
    }

    /* Database table boilerplate */

    // Database table name
    private static final String TABLE_NAME = "StockTable";

    // Database columns for StockTable. They are all strings since when executing the sql command it must be in the form of a string
    private static final String STOCK_SYMBOL = "StockSymbol";
    private static final String COMPANY_NAME = "CompanyName";


    // Command to create our stock table
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + STOCK_SYMBOL + " TEXT not null unique, "
                    + COMPANY_NAME + " TEXT not null)";


    // Create database tables here
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute SQL_CREATE_TABLE command to create the stock table
        db.execSQL(SQL_CREATE_TABLE);

        Log.d(TAG, "onCreate: Stock Table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // Add a stock
    public void addStock(String stockSymbol, String companyName) {
        // Create a ContentValues object which will store the information of the stock in key-value form and can be used in the insert command of the database
        ContentValues value = new ContentValues();

        // Put into the ContentValues the corresponding column values from the stock argument as the values and the column names as the keys
        value.put(STOCK_SYMBOL, stockSymbol);
        value.put(COMPANY_NAME, companyName);

        // Get the key return value after calling insert to put data into table. If key is -1, an error occurred
        long key = db.insert(TABLE_NAME, null, value);
        Log.d(TAG, "addStock: A new stock has been added with key: " + key);
    }

    // Delete a stock
    public void deleteStock(String stockSymbol) {
        // return value reads the number of rows deleted
        int r = db.delete(TABLE_NAME, STOCK_SYMBOL + " = ?", new String[]{stockSymbol});
        Log.d(TAG, "deleteStock: A stock has been deleted. This many rows has been deleted: " + r);
    }

    /* Load up all the stocks. Will return an ArrayList of Strings containing the stock symbols which will then be used along with the HashMap as arguments for AsyncStockDownloader.
       This will be done in the Main Activity
    */

    public ArrayList<String[]> getStocks() {
        // Initialize an array list. This will return an arraylist of an array of Strings. Format of each element {symb, name} in the arraylist
        ArrayList<String[]> r = new ArrayList<String[]>();


        // Set a cursor object equal to our query. We will use this object to traverse through each row of the query
        Cursor cursor = db.query(
                TABLE_NAME, // First argument if the table name
                new String[] {STOCK_SYMBOL, COMPANY_NAME}, // Second argument is an array of the columns we want to query
                null,
                null,
                null,
                null,
                null
                );


        // If cursor is not null from query
        if (cursor != null) {
            // Move the cursor to the first row. Returns true if cursor is now pointing to the first row of query
            if (cursor.moveToFirst()) {

                // Go through each row
                for (int i = 0; i < cursor.getCount(); i++) {
                    // Get the stock symbol
                    String stockSymbol = cursor.getString(0);

                    // Get the stock's company name
                    String companyName = cursor.getString(1);

                    // Add to the arrayList
                    r.add(new String[]{stockSymbol, companyName});

                    // Move cursor to the next row
                    cursor.moveToNext();
                }

                // Close the cursor after going through each row
                cursor.close();
            }
        }

        Log.d(TAG, "getStocks: Stocks has been loaded from the Database");
        // Return the array list
        return r;
    }


    // Dump to log
    public void dumpDbToLog() {
        // Set a cursor object equal to our query. We will use this object to traverse through each row of the query
        Cursor cursor = db.query(
                TABLE_NAME, // First argument if the table name
                new String[] {STOCK_SYMBOL, COMPANY_NAME}, // Second argument is an array of the columns we want to query
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.d(TAG, "dumpDbToLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                for (int i = 0; i < cursor.getCount(); i++) {
                    String stockSymbol = cursor.getString(0);
                    String companyName = cursor.getString(1);

                    Log.d(TAG, "dumpDbToLog: " +
                            String.format("%s %-18s",  STOCK_SYMBOL + ": ", stockSymbol) +
                            String.format("%s %-18s",  COMPANY_NAME + ": ", companyName)
                    );

                    cursor.moveToNext();

                }
                Log.d(TAG, "dumpDbToLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            }

            cursor.close();
        }
    }

    public void shutDown() {
        db.close();
    }
}
