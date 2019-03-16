package com.example.stockwatch;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AsyncNameDownloader extends AsyncTask<String, Double, String> {

    // URL to get all the stocks and their information
    private static final String DATA_URL = "https://api.iextrading.com/1.0/ref-data/symbols";

    // Tag for logging
    private static final String TAG = "AsyncNameDownloader";


    // Main Activity
    MainActivity mainActivity;

    public AsyncNameDownloader(MainActivity ma) {
        this.mainActivity = ma;
    }

    @Override
    protected String doInBackground(String... strings) {
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

            Log.d(TAG, "doInBackground: String Builder sb => " + sb.toString());

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

        // Create a HashMap of symbol:name from the JSON Array
        HashMap<String, String> hashMap = this.parseJSON(s);

        // Initialize the HashMap in the Main Activity
        this.mainActivity.setHashMap(hashMap);

    }

    private HashMap<String, String> parseJSON(String s) {
        HashMap<String, String> r = new HashMap<>(8750);

        try {
            // Store the JSON Array String into a JSONArray object to parse through it
            JSONArray jsonArray = new JSONArray(s);

            // Loop through each object in the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {

                // Store the JSON Object from the array
                JSONObject stockObject = jsonArray.getJSONObject(i);

                // Some stocks have no company names so those will not be included in the HashMap
                String companyName = stockObject.getString("name");

                // If there is a company name for the stock
                if (!companyName.isEmpty()) {
                    // Get the stock symbol
                    String stockSymbol = stockObject.getString("symbol");

                    // Store stock symbol and company name into the HashMap
                    r.put(stockSymbol, companyName);
                }

            }
            return r;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
