package com.example.inspirationrewards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    // Request code for edit profile
    static int EDIT_PROFILE_CODE = 0;

    // List of the user's rewards
    List<Reward> myRewards;

    // UserProfile object representing the user's profile information
    UserProfile currUser;

    // Name TextView
    TextView name;

    // Username TextView
    TextView username;

    // Location TextView
    TextView location;

    // Profile ImageView
    ImageView profileImage;

    // Points available to reward
    TextView numPoints;

    // Department TextView
    TextView department;

    // Position TextView
    TextView position;

    // Story TextView
    TextView story;

    // Total Reward Points TextView
    TextView totalPointsView;

    // Recycler View for the rewards
    RecyclerView recyclerView;

    // Rewards Adapter
    RewardAdapter rewardAdapter;

    // Reward History. Text View that shows the number of rewards
    TextView rewardHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set title bar of Activity to Your Profile
        getSupportActionBar().setTitle("Your Profile");

        // Initialize UserProfile object
        currUser = (UserProfile) getIntent().getSerializableExtra("userProfile");

        // Initialize rewards list
        myRewards = currUser.getMyRewards();

        // Set text for name
        name = findViewById(R.id.profile_fullname);
        name.setText(currUser.getLastName() + ", " + currUser.getFirstName());

        // Set text for username
        username = findViewById(R.id.profile_username);
        username.setText("(" + currUser.getUsername() + ")");

        // Set text for location
        location = findViewById(R.id.profile_location);
        location.setText(currUser.getLocation());

        // Set profile image
        Bitmap bitmap = BitmapFactory.decodeByteArray(currUser.getProfileImage(), 0, currUser.getProfileImage().length);
        profileImage = findViewById(R.id.profile_image);
        profileImage.setImageBitmap(bitmap);

        // Set text for the amount of points the user has to reward others
        numPoints = findViewById(R.id.profile_num_points_to_award);
        numPoints.setText(Integer.toString(currUser.getPointsToAward()));

        // Set text for the department of the user
        department = findViewById(R.id.profile_department_type);
        department.setText(currUser.getDepartment());

        // Set text for the position of the user
        position = findViewById(R.id.profile_position_type);
        position.setText(currUser.getPosition());

        // Set text for the story of the user
        story = findViewById(R.id.profile_your_story);
        story.setText(currUser.getStory());

        // Set total points awarded
        totalPointsView = findViewById(R.id.profile_total_points);
        int totalPoints = 0;
        for (int i = 0; i < myRewards.size();  i++) {
            Reward currentReward = myRewards.get(i);
            totalPoints += currentReward.getValue();
        }
        totalPointsView.setText(Integer.toString(totalPoints));

        // Initialize Recycler View
        recyclerView = findViewById(R.id.profile_rewards_list);

        // Assign LayoutManager to recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter
        rewardAdapter = new RewardAdapter(myRewards, this);

        // Set adapter to recycler view
        recyclerView.setAdapter(rewardAdapter);

        // Disable scrolling for recycler view
        recyclerView.setNestedScrollingEnabled(false);

        // Set up reward history
        rewardHistory = findViewById(R.id.profile_reward_text);
        String rewardHistoryStr = rewardHistory.getText().toString();

        // User has rewards
        if (myRewards.size() > 0) {
            rewardHistoryStr += " (" + myRewards.size() + "):";
        }

        // User has no rewards
        else {
            rewardHistoryStr += ":";
        }

        // Set text for reward history
        rewardHistory.setText(rewardHistoryStr);


    }

    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Function that will handle callback when menu items are selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User wishes to edit their profile
        if (item.getItemId() == R.id.edit_icon) {
            Toast.makeText(this, "Go to edit profile activity", Toast.LENGTH_SHORT).show();
            Intent goToEditProfileIntent = new Intent(this, EditProfile.class);

            // Add UserProfile object of the current user to the intent
            goToEditProfileIntent.putExtra("userProfile", currUser);
            startActivityForResult(goToEditProfileIntent, EDIT_PROFILE_CODE);
        }

        // User wishes to go to the leaderboards
        else if (item.getItemId() == R.id.leaderboard_icon) {
            Toast.makeText(this, "Go to leaderboards activity", Toast.LENGTH_SHORT).show();
            Intent goToLeaderboardIntent = new Intent(this, Leaderboard.class);

            goToLeaderboardIntent.putExtra("userProfile", currUser);
            startActivity(goToLeaderboardIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // User went to the EditProfile Activity
        if (requestCode == EDIT_PROFILE_CODE) {
            // User updated profile
            if (resultCode == 1) {
                currUser = (UserProfile) data.getSerializableExtra("profile");

                // Set values to new profile

                // Set text for name
                name = findViewById(R.id.profile_fullname);
                name.setText(currUser.getLastName() + ", " + currUser.getFirstName());

                // Set text for username
                username = findViewById(R.id.profile_username);
                username.setText("(" + currUser.getUsername() + ")");

                // Set text for location
                location = findViewById(R.id.profile_location);
                location.setText(currUser.getLocation());

                // Set profile image
                Bitmap bitmap = BitmapFactory.decodeByteArray(currUser.getProfileImage(), 0, currUser.getProfileImage().length);
                profileImage = findViewById(R.id.profile_image);
                profileImage.setImageBitmap(bitmap);

                // Set text for the amount of points the user has to reward others
                numPoints = findViewById(R.id.profile_num_points_to_award);
                numPoints.setText(Integer.toString(currUser.getPointsToAward()));

                // Set text for the department of the user
                department = findViewById(R.id.profile_department_type);
                department.setText(currUser.getDepartment());

                // Set text for the position of the user
                position = findViewById(R.id.profile_position_type);
                position.setText(currUser.getPosition());

                // Set text for the story of the user
                story = findViewById(R.id.profile_your_story);
                story.setText(currUser.getStory());

            }
        }


    }
}
