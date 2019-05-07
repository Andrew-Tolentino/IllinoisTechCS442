package com.example.newsgateway;

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
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class AsyncGetArticles extends AsyncTask <String, Integer, String> {

    private String id;
    private ArrayList<Article> arrayList;

    public AsyncGetArticles(String id, ArrayList<Article> arrayList) {
        this.id = id;
        this.arrayList = arrayList;
    }

    @Override
    protected String doInBackground(String... strings) {
        String articileUrl = "https://newsapi.org/v2/everything?sources=" + this.id + "&language=en&pageSize=100&apiKey=a5fd50e55ace4aba9ef96c1d5f59a165";
        Log.d("GetArticles", "id: " + this.id);

        BufferedReader reader = null;
        HttpURLConnection conn = null;

        // We use a StringBuilder to efficiently append Strings without being to slow
        StringBuilder sb = new StringBuilder();

        try {
            Uri uri = Uri.parse(articileUrl);
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
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            Log.d("GetArticles", "Object: " + s);
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("articles");

            // Fill up array list
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempObj = jsonArray.getJSONObject(i);

                String author = "";
                String title = "";
                String description = "";
                String url = "";
                String imageUrl = "https://images-assets.nasa.gov/image/6900952/does_not_exist.jpg";
                String date = "";

                if (!tempObj.isNull("author")) {
                    author = tempObj.getString("author");
                }

                if (!tempObj.isNull("title")) {
                    title = tempObj.getString("title");
                }

                if (!tempObj.isNull("description")) {
                    description = tempObj.getString("description");
                }

                if (!tempObj.isNull("url")) {
                    url = tempObj.getString("url");
                }

                if (!tempObj.isNull("urlToImage")) {
                    imageUrl = tempObj.getString("urlToImage");
                }

                if (!tempObj.isNull("publishedAt")) {
                    date = tempObj.getString("publishedAt");
                }

                arrayList.add(new Article(author, title, description, url, imageUrl, date, i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
