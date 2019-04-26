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
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class AsyncTaskGetAllProfiles extends AsyncTask<String, Integer, String> {
    private static final String BASE_URL = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String STUDENT_ID =  "A20394982";

    // Success represents the status of the request
    private static boolean success = false;

    // Reference to Profile Activity
    Leaderboard leaderboard;

    // User's profile
    UserProfile currUser;

    public AsyncTaskGetAllProfiles(Leaderboard leaderboard, UserProfile currUser) {
        this.leaderboard = leaderboard;
        this.currUser = currUser;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            if (success) {
                // Add all users
                try {
                    JSONArray array = new JSONArray(s);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject tempObj = array.getJSONObject(i);

                        String firstName = tempObj.getString("firstName");
                        String lastName = tempObj.getString("lastName");
                        String username = tempObj.getString("username");
                        String department = tempObj.getString("department");
                        String story = tempObj.getString("story");
                        String position = tempObj.getString("position");
                        String password = "";
                        int pointsToAward = tempObj.getInt("pointsToAward");
                        boolean admin = tempObj.getBoolean("admin");


                        byte[] profileImage = Base64.decode(tempObj.getString("imageBytes"), Base64.DEFAULT);


                        String location = tempObj.getString("location");
                        Log.d("GetProfiles", "onPostExecute: " + firstName + " " + lastName + ": " + tempObj.isNull("rewards"));


                        // Go through Rewards array if it is not null
                        if (!tempObj.isNull("rewards")) {
                            JSONArray rewardsArr = new JSONArray(tempObj.getString("rewards"));

                            // List of the user's rewards
                            List<Reward> myRewards = new ArrayList<>();

                            for (int j = 0; j < rewardsArr.length(); j++) {
                                JSONObject rewardObj = rewardsArr.getJSONObject(j);

                                String rewardUsername = rewardObj.getString("username");
                                String rewardName = rewardObj.getString("name");
                                String rewardDate = rewardObj.getString("date");
                                String rewardNote = rewardObj.getString("notes");
                                int rewardVal = rewardObj.getInt("value");

                                myRewards.add(new Reward(rewardUsername, rewardName, rewardDate, rewardNote, rewardVal));
                            }
                            // Create UserProfile object and add it to the list in Leaderboard Activity
                            UserProfile newProfile = new UserProfile(firstName, lastName, username, department, story, position, password, pointsToAward, admin, profileImage, location, myRewards);
                            this.leaderboard.addProfile(newProfile);
                        }

                        // Rewards array is null
                        else {
                            // List of the user's rewards
                            List<Reward> myRewards = new ArrayList<>();
                            UserProfile newProfile = new UserProfile(firstName, lastName, username, department, story, position, password, pointsToAward, admin, profileImage, location, myRewards);
                            this.leaderboard.addProfile(newProfile);
                        }




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.d("GetProfiles", "onPostExecuteFail: " + s);
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String updateProfileURL = BASE_URL + "/allprofiles";
        BufferedReader reader = null;
        HttpURLConnection conn = null;

        try {
            Uri uri = Uri.parse(updateProfileURL);
            URL url = new URL(uri.toString());
            conn = (HttpURLConnection) url.openConnection();

            // Get JSON body
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

        try {
            body.put("studentId", STUDENT_ID);
            body.put("username", currUser.getUsername());
            body.put("password", currUser.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return body;
    }
}
