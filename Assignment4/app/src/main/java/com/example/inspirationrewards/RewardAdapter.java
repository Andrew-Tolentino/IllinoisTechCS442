package com.example.inspirationrewards;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardViewHolder> {
    private List<Reward> rewardList;
    private Profile activity;

    public RewardAdapter(List<Reward> rList, Profile activity) {
        this.rewardList = rList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reward_layout, viewGroup, false);
        return new RewardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder rewardViewHolder, int i) {
        Reward currReward = rewardList.get(i);

        // Initialize ViewHolder content
        rewardViewHolder.rewardDate.setText(currReward.getDate());
        rewardViewHolder.rewardNote.setText(currReward.getRewardNote());
        rewardViewHolder.rewardValue.setText(Integer.toString(currReward.getValue()));
        rewardViewHolder.rewardName.setText(currReward.getName());
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }
}
