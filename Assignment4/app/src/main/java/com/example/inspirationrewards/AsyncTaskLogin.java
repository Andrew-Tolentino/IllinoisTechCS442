package com.example.inspirationrewards;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class AsyncTaskLogin extends AsyncTask <String, Double, String> {
    private static final String BASE_URL = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String STUDENT_ID =  "A20394982";

    // Success represents the status of the request
    private static boolean success = false;

    // This will be the login activity
    private MainActivity ma;

    // User's username
    private String username;

    // User's password
    private String password;

    // Response code
    private int responseCode;

    // City
    private String city;

    // State
    private String state;


    AsyncTaskLogin(MainActivity ma, String username, String password, String city, String state) {
        this.ma = ma;
        this.username = username;
        this.password = password;
        this.city = city;
        this.state = state;

    }

    @Override
    protected void onPostExecute(String s) {
       if (s != null) {
           // Convert s into a JSON object

           // The request succeeded
           if (success) {
               Log.d("AsyncTaskLogin", "onPostExecute: " + s);
               try {
                   // Create user's profile object
                   JSONObject userObject = new JSONObject(s);

                   // First name
                   String firstName = userObject.getString("firstName");

                   // Last name
                   String lastName = userObject.getString("lastName");

                   // Username
                   String username = userObject.getString("username");

                   // Department
                   String department = userObject.getString("department");

                   // Story
                   String story = userObject.getString("story");

                   // Position
                   String position = userObject.getString("position");

                   // Password
                   String password = userObject.getString("password");

                   // Award Points
                   int pointsToAward = userObject.getInt("pointsToAward");

                   // Admin status
                   boolean admin = userObject.getBoolean("admin");

                   // Profile Image is a Bitmap
                   byte[] profileImage = decodeBase64(userObject.getString("imageBytes"));

                   // Location
                   String location = city + ", " + state;

                   // List of the user's rewards
                   List<Reward> myRewards = new ArrayList<>();

                   JSONArray arr = new JSONArray(userObject.getString("rewards"));

                   // Go through array
                   for (int i = 0; i < arr.length(); i++) {
                       JSONObject currObject = arr.getJSONObject(i);
                       Reward newReward = new Reward(currObject.getString("username"), currObject.getString("name"),currObject.getString("date"), currObject.getString("notes"), currObject.getInt("value") );
                       myRewards.add(newReward);

                   }

                   // Create UserProfile
                   UserProfile currUser = new UserProfile(firstName, lastName, username, department, story, position, password, pointsToAward, admin, profileImage, location, myRewards);
                   ma.goToProfile(currUser);

               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
           else {
               Log.d("AsyncTaskLogin", "onPostExecute: " + s);
               Toast.makeText(ma, "Username or password is incorrect.", Toast.LENGTH_SHORT).show();

               // Hide progress bar
               ma.progressBar.setVisibility(View.GONE);

           }
       }
    }

    @Override
    protected String doInBackground(String... strings) {
        String loginURL = BASE_URL + "/login";
        BufferedReader reader = null;
        HttpURLConnection conn = null;

        // Set up JSON body
        JSONObject body = getBody();

        // Set the request method and request properties
        Uri uri = Uri.parse(loginURL);
        URL url = null;
        try {
             url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // Set the request method and request properties
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OutputStreamWriter ow = null;
        try {
            ow = new OutputStreamWriter(conn.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ow.write(body.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ow.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get response code
        try {
            StringBuilder result = new StringBuilder();
            responseCode = conn.getResponseCode();

            Log.d("AsyncTaskLogin", "doInBackground response code: " + responseCode);

            // If request succeeds
            if (responseCode == HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;

                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                // Set success to true
                success = true;

                // Return results
                return result.toString();
            }

            // If request fails
            else {
                // Find error
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String line;

                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                // Return results
                return result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private JSONObject getBody() {
        JSONObject body = new JSONObject();

        // Place in username, passowrd, and studentId into body
        try {
            body.put("studentId", STUDENT_ID);
            body.put("username", this.username);
            body.put("password", this.password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
    }


    // Function to decode Base64 encoding
    public byte[] decodeBase64(String encodedStr) {
        byte[] imageBytes = Base64.decode(encodedStr, Base64.DEFAULT);

        return imageBytes;
    }
}
