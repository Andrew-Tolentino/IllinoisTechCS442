package com.example.inspirationrewards;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard extends AppCompatActivity implements View.OnClickListener{

    // Request code to view profile
    private static int REQ_CODE = 0;

    // List for profiles
    List<UserProfile> profileList = new ArrayList<UserProfile>();

    // Current user profile
    UserProfile currUser;

    // Recycler View
    private RecyclerView recyclerView;

    // Adapter for recycler view
    private LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Set title bar of Activity to Inspiration Leaderboard
        getSupportActionBar().setTitle("Inspiration Leaderboard");

        // Initialize current user's profile
        currUser = (UserProfile) getIntent().getSerializableExtra("userProfile");

        // Initialize adapter
        leaderboardAdapter = new LeaderboardAdapter(profileList, this, currUser);

        // Get all profiles
        AsyncTaskGetAllProfiles asyncTaskGetAllProfiles = new AsyncTaskGetAllProfiles(this, currUser);
        asyncTaskGetAllProfiles.execute();

        // Initialize recycler view
        recyclerView = findViewById(R.id.leaderboard_recycler_view);

        // Assign LayoutManager to recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set leaderboardAdaper to recycler view
        recyclerView.setAdapter(leaderboardAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User uses up navigation
         if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Function to add users to profileList
    public void addProfile(UserProfile addUser) {
        profileList.add(addUser);

        // Sort list
        Collections.sort(profileList, new Comparator<UserProfile>() {
            @Override
            public int compare(UserProfile o1, UserProfile o2) {

                // First argument's total rewarded points
                List<Reward> o1Rewards = o1.getMyRewards();
                int o1Total = 0;
                for (int i = 0; i < o1Rewards.size(); i++) {
                    Reward currReward = o1Rewards.get(i);
                    o1Total += currReward.getValue();
                }

                // Second argument's total rewarded points
                List<Reward> o2Rewards = o2.getMyRewards();
                int o2Total = 0;
                for (int i = 0; i < o2Rewards.size(); i++) {
                    Reward currReward = o2Rewards.get(i);
                    o2Total += currReward.getValue();
                }

                if (o1Total > o2Total) {
                    return -1;
                }

                else if (o1Total < o2Total) {
                    return 1;
                }

                return 0;
            }
        });

        // If the argument is the user's profile for the leaderboards
        if (addUser.getUsername().equals(currUser.getUsername())) {
            // Color all text fields

        }

        // Tell adapter the change in data
        leaderboardAdapter.notifyDataSetChanged();
    }

    // Function to go ViewProfile activity. Pass in that user's  profile in the intent
    @Override
    public void onClick(View v) {

        UserProfile viewUserProfile = profileList.get(recyclerView.getChildLayoutPosition(v));

        // The user pressed is not the same user logged in
        if (!viewUserProfile.getUsername().equals(currUser.getUsername())) {
            Intent goToViewProfileIntent = new Intent(this, ViewProfile.class);


            // Get all rewarded points
            List<Reward> tempRewardList = viewUserProfile.getMyRewards();
            int totalRewardedPoints = 0;
            for (int i = 0; i < tempRewardList.size(); i++) {
                Reward currReward = tempRewardList.get(i);
                totalRewardedPoints += currReward.getValue();
            }


            goToViewProfileIntent.putExtra("profile", viewUserProfile);
            goToViewProfileIntent.putExtra("totalRewardedPoints", totalRewardedPoints);
            goToViewProfileIntent.putExtra("username", currUser.getUsername());
            goToViewProfileIntent.putExtra("password", currUser.getPassword());

            startActivityForResult(goToViewProfileIntent, REQ_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // User viewed another user's profile
        if (requestCode == REQ_CODE) {
            // User awarded someone
            if (resultCode == 1) {
                Toast.makeText(this, "Add Reward succeeded", Toast.LENGTH_SHORT).show();
                // Reload users
                profileList.clear();
                AsyncTaskGetAllProfiles asyncTaskGetAllProfiles = new AsyncTaskGetAllProfiles(this, currUser);
                asyncTaskGetAllProfiles.execute();
            }

        }
    }
}
