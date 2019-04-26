package com.example.inspirationrewards;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class AsyncTaskGiveReward extends AsyncTask <String, Integer, String> {
    private static final String BASE_URL = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String STUDENT_ID =  "A20394982";

    // Success represents the status of the request
    private static boolean success = false;

    // Ref to the ViewProfile activity
    private ViewProfile ref;

    // Username of the user to reward
    private String toRewardUsername;

    // Name of the user to reward
    private String toRewardName;

    // Date of reward
    private String date;

    // Note the rewarder gave
    private String note = "";

    // Amount of reward points given
    private int value;

    // Username of the user who gave the reward
    private String rewarderUsername;

    // Password of the user who gave the reward
    private String rewarderPassword;

    public AsyncTaskGiveReward(ViewProfile activity, String rewardedUsername, String rewardedName, String date, String note, int rewardValue, String rewarderUsername, String rewarderPassword) {
        this.ref = activity;
        this.toRewardUsername = rewardedUsername;
        this.toRewardName = rewardedName;
        this.date = date;
        this.note = note;
        this.value = rewardValue;
        this.rewarderUsername = rewarderUsername;
        this.rewarderPassword = rewarderPassword;
    }

    @Override
    protected void onPostExecute(String s) {
        // Request was a success
        if (success) {
            // Result code 1 means the user successfully added an award
            ref.setResult(1);
            ref.finish();
        }
        else {
            Log.d("Reward", "onPostExecute: " + s);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String rewardURL = BASE_URL + "/rewards";
        BufferedReader reader = null;
        HttpURLConnection conn = null;

        try {
            Uri uri = Uri.parse(rewardURL);
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

        // Target inner object
        JSONObject target = new JSONObject();
        try {
            target.put("studentId", STUDENT_ID);
            target.put("username", this.toRewardUsername);
            target.put("name", this.toRewardName);
            target.put("date", this.date);
            target.put("notes", this.note);
            target.put("value", this.value);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Source inner object
        JSONObject source = new JSONObject();
        try {
            source.put("studentId", STUDENT_ID);
            source.put("username", this.rewarderUsername);
            source.put("password", this.rewarderPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Place both objects into body
        try {
            body.put("target", target);
            body.put("source", source);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body;
    }
}
