package com.example.inspirationrewards;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class AsyncTaskCreateProfile extends AsyncTask<String, Integer, String> {

    private static final String BASE_URL = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String STUDENT_ID =  "A20394982";

    // Success represents the status of the request
    private static boolean success = false;

    // The username of the new user
    private String username;

    // The password of the new user
    private String password;

    // The first name of the new user
    private String firstName;

    // The last name of the new user
    private String lastName;

    // The department of the new user
    private String department;

    // The story of the new user
    private String story;

    // The position of the new user
    private String position;

    // If the new user is an admin;
    private boolean isAdmin;

    // The location the new user created their account
    private String location;

    // The encoded profile image of the user
    private String profileImage;


    // New users start of with 1000 award points and no rewards record since they are new


    AsyncTaskCreateProfile(String username, String password, String firstName, String lastName, String department, String story, String position, boolean admin, String location, String image) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.story = story;
        this.position = position;
        this.isAdmin =  admin;
        this.location = location;
        this.profileImage = image;
    }

    @Override
    protected String doInBackground(String... params) {
        String createProfileURL = BASE_URL + "/profiles";
        BufferedReader reader = null;
        HttpURLConnection conn = null;

        try {
            Uri uri = Uri.parse(createProfileURL);
            URL url = new URL(uri.toString());

            conn = (HttpURLConnection) url.openConnection();

            // Set up JSON body
            JSONObject body = getBody();

            // Set the request method and request properties
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            // Set up output stream to send body
            Log.d("CreateProfile", "doInBackground body: " + body.toString());
            OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
            ow.write(body.toString());
            ow.close();

            // Get response code
            int responseCode = conn.getResponseCode();
            Log.d("CreateProfile", "doInBackgroundCode: " + responseCode);

            StringBuilder result = new StringBuilder();

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


        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("CreateProfile", "Error: " + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("CreateProfile", "Error: " + e.toString());
        } finally {
            // Close everything
            if (conn != null) {
                conn.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("CreateProfile", "Error: " + e.toString());
                }
            }
        }
        return null;
    }

    private JSONObject getBody() {
        JSONObject body = new JSONObject();

        // Add information about the new user and studentID to body
        try {
            body.put("studentId", STUDENT_ID);
            body.put("username", this.username);
            body.put("password", this.password);
            body.put("firstName", this.firstName);
            body.put("lastName", this.lastName);

            // For new users they start off with 1000 points
            body.put("pointsToAward", 1000);

            body.put("department", this.department);
            body.put("story", this.story);
            body.put("position", this.position);
            body.put("admin", this.isAdmin);
            body.put("location", this.location);
            body.put("imageBytes", this.profileImage);

            // For new users starts off an empty array of rewards
            body.put("rewardRecords", new JSONArray());


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("CreateProfile", "getBody Error: " + e.toString());

        }


        return body;
    }

    @Override
    protected void onPostExecute(String s) {
        // The request succeeded
        if (success) {
            Log.d("CreateProfile", "onPostExecute: Success!");
        }
        else {
            Log.d("CreateProfile", "onPostExecute: Failed!");
        }

        if (s != null) {
            Log.d("CreateProfile", "onPostExecute: " + s);
        }
    }
}
