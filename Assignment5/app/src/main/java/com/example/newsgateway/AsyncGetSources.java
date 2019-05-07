package com.example.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class AsyncGetSources extends AsyncTask <String, Integer, String> {
    private static String SOURCE_URL = "https://newsapi.org/v2/sources?language=en&country=us&apiKey=a5fd50e55ace4aba9ef96c1d5f59a165";

    private MainActivity ma;

    public AsyncGetSources(MainActivity mainActivity) {
        this.ma = mainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {
        BufferedReader reader = null;
        HttpURLConnection conn = null;

        // We use a StringBuilder to efficiently append Strings without being to slow
        StringBuilder sb = new StringBuilder();
        try {
            Uri uri = Uri.parse(SOURCE_URL);
            URL url = new URL(uri.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            /* Parse through the JSON and convert it into a String */

            // Get the raw bytes of the output
            InputStream is = conn.getInputStream();

            // Decode the bytes into the default charset and user a buffer to go through them efficiently
            reader = new BufferedReader((new InputStreamReader(is)));

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("GET_SOURCE", "onPostExecute: " + s);
        this.parseJSON(s);


    }

    private void parseJSON(String jsonString) {
        try {
            ArrayList<NewsSource> newsSourceArrayList = new ArrayList<NewsSource>();
            ArrayList<String> categoryList = new ArrayList<String>();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("sources");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempObj = new JSONObject(jsonArray.getString(i));
                NewsSource newsSource = new NewsSource(tempObj.getString("id"), tempObj.getString("name"), tempObj.getString("description"),
                        tempObj.getString("url"), tempObj.getString("category"), tempObj.getString("language"), tempObj.getString("country"));
                newsSourceArrayList.add(newsSource);

                if (categoryList.indexOf(tempObj.getString("category")) == -1) {
                    categoryList.add(tempObj.getString("category"));
                }
            }

            this.ma.addSource(newsSourceArrayList, categoryList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
