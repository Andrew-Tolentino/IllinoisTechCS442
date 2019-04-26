package com.example.inspirationrewards;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RewardViewHolder extends RecyclerView.ViewHolder {
    public TextView rewardDate;
    public TextView rewardName;
    public TextView rewardValue;
    public TextView rewardNote;

    public RewardViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize Views
        rewardDate = itemView.findViewById(R.id.reward_date);
        rewardName = itemView.findViewById(R.id.reward_name);
        rewardValue = itemView.findViewById(R.id.reward_value);
        rewardNote = itemView.findViewById(R.id.reward_note);
    }
}
