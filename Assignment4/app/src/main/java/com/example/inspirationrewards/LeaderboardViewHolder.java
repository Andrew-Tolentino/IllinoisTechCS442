package com.example.inspirationrewards;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

    public TextView leaderboardName;
    public TextView leaderboardPoints;
    public TextView leaderboardJobAndPosition;
    public ImageView leaderboardImage;

    public LeaderboardViewHolder(@NonNull View itemView) {
        super(itemView);

        leaderboardName = itemView.findViewById(R.id.leaderboard_viewholder_name);
        leaderboardPoints = itemView.findViewById(R.id.leaderboard_viewholder_points);
        leaderboardJobAndPosition = itemView.findViewById(R.id.leaderboard_viewholder_position_and_department);
        leaderboardImage = itemView.findViewById(R.id.leaderboard_viewholder_image);

    }
}
