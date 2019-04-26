package com.example.inspirationrewards;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class AsyncTaskUpdateLocation extends AsyncTask <String,  Integer, String> {
    private static final String BASE_URL = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String STUDENT_ID =  "A20394982";

    // Success represents the status of the request
    private static boolean success = false;

    // UserProfile of current user
    UserProfile currUser;

    public AsyncTaskUpdateLocation(UserProfile profile) {
        this.currUser = profile;
    }

    @Override
    protected void onPostExecute(String s) {
        if (success) {
            Log.d("Update Location", "onPostExecute: Success" + s);
        }
        else if (s != null) {
            Log.d("Update Location", "onPostExecute: Fail" + s);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String updateProfileURL = BASE_URL + "/profiles";
        BufferedReader reader = null;
        HttpURLConnection conn = null;

        try {
            Uri uri = Uri.parse(updateProfileURL);
            URL url = new URL(uri.toString());
            conn = (HttpURLConnection) url.openConnection();

            // Get JSON body
            JSONObject body = getBody();

            // Set the request method and request properties
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            // Set up output stream to send body
            OutputStreamWriter ow = new OutputStreamWriter(conn.getOutputStream());
            ow.write(body.toString());
            ow.close();

            // Get response code
            int responseCode = conn.getResponseCode();

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private JSONObject getBody() {
        JSONObject body = new JSONObject();

        try {
            body.put("studentId", STUDENT_ID);
            body.put("username", this.currUser.getUsername());
            body.put("password", this.currUser.getPassword());
            body.put("firstName", this.currUser.getFirstName());
            body.put("lastName", this.currUser.getLastName());
            body.put("pointsToAward", this.currUser.getPointsToAward());
            body.put("department", this.currUser.getDepartment());
            body.put("story", this.currUser.getStory());
            body.put("position", this.currUser.getPosition());
            body.put("admin", this.currUser.getAdmin());
            body.put("location", this.currUser.getLocation());
            body.put("imageBytes", Base64.encodeToString(currUser.getProfileImage(), Base64.DEFAULT));

            // Put Rewards as JSON Array
            JSONArray array = new JSONArray();
            List<Reward> currRewardsList = currUser.getMyRewards();

            for (int i = 0; i < currRewardsList.size(); i++) {
                JSONObject tempObject = new JSONObject();
                Reward currReward = currRewardsList.get(i);

                tempObject.put("name", currReward.getName());
                tempObject.put("date", currReward.getDate());
                tempObject.put("notes", currReward.getRewardNote());
                tempObject.put("value", currReward.getValue());

                array.put(tempObject);
            }

            body.put("rewardsRecords", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return body;
    }
}
