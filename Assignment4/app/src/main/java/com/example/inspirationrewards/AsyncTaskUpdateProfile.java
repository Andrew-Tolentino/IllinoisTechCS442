package com.example.inspirationrewards;

import android.content.Intent;
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
import java.net.URI;
import java.net.URL;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class AsyncTaskUpdateProfile extends AsyncTask <String, Integer, String> {
    private static final String BASE_URL = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String STUDENT_ID =  "A20394982";

    // Success represents the status of the request
    private static boolean success = false;

    // Use to reference Edit Profile Activity
    private EditProfile editProfile;

    // UserProfile containing information about the user
    UserProfile currUser;



    public AsyncTaskUpdateProfile(EditProfile editProfile, UserProfile currUser) {
        this.editProfile = editProfile;
        this.currUser = currUser;

    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            if (success) {
                Intent i = new Intent(editProfile, Profile.class);
                i.putExtra("profile", this.currUser);
                editProfile.setResult(1, i);
                editProfile.finish();
            }
            else {
                Log.d("Update", "onPostExecuteFail: " + s);

            }
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
            body.put("username", currUser.getUsername());
            body.put("password", currUser.getPassword());
            body.put("firstName", currUser.getFirstName());
            body.put("lastName", currUser.getLastName());
            body.put("pointsToAward", currUser.getPointsToAward());
            body.put("department", currUser.getDepartment());
            body.put("story", currUser.getStory());
            body.put("position", currUser.getPosition());
            body.put("admin", currUser.getAdmin());
            body.put("location", currUser.getLocation());
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
