package com.example.inspirationrewards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardViewHolder> {

    private List<UserProfile> leaderboardProfiles;
    private Leaderboard activity;
    private UserProfile currUser;

    public LeaderboardAdapter(List<UserProfile> list, Leaderboard activity, UserProfile profile) {
        this.leaderboardProfiles = list;
        this.activity = activity;
        this.currUser = profile;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leaderboard_viewholder, viewGroup, false);

        itemView.setOnClickListener(this.activity);

        return new LeaderboardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder leaderboardViewHolder, int i) {
        UserProfile currentProfile = leaderboardProfiles.get(i);

        // Initialize profile image
        Bitmap bitmap = BitmapFactory.decodeByteArray(currentProfile.getProfileImage(), 0, currentProfile.getProfileImage().length);
        leaderboardViewHolder.leaderboardImage.setImageBitmap(bitmap);

        // Initialize profile name
        leaderboardViewHolder.leaderboardName.setText(currentProfile.getLastName() + ", " + currentProfile.getFirstName());

        // Initialize points
        int totalRewardPoints = 0;
        for (int j = 0; j < currentProfile.getMyRewards().size(); j++) {
            totalRewardPoints += currentProfile.getMyRewards().get(j).getValue();
        }
        leaderboardViewHolder.leaderboardPoints.setText(Integer.toString(totalRewardPoints));

        // Initialize position and department
        leaderboardViewHolder.leaderboardJobAndPosition.setText(currentProfile.getPosition() + ", " + currentProfile.getDepartment());

        // If the current profile is the user's profile
        if (currentProfile.getUsername().equals(currUser.getUsername())) {
            // Set the colors for the text fields
            leaderboardViewHolder.leaderboardName.setTextColor(Color.parseColor("#53b7cc"));
            leaderboardViewHolder.leaderboardJobAndPosition.setTextColor(Color.parseColor("#53b7cc"));
            leaderboardViewHolder.leaderboardPoints.setTextColor(Color.parseColor("#53b7cc"));
        }

    }

    @Override
    public int getItemCount() {
        return leaderboardProfiles.size();
    }
}
