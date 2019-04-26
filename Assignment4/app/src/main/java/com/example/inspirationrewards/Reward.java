package com.example.inspirationrewards;

import java.io.Serializable;

public class Reward implements Serializable {
    // The username of the rewarder
    private String username;

    // The name of the rewarder
    private String name;

    // The date of the reward
    private String date;

    // The note of the reward
    private String rewardNote;

    // The value of the reward
    private int value;

    public Reward(String username, String name, String date, String rewardNote, int val) {
        // Initialize all attributes
        this.username = username;
        this.name = name;
        this.date = date;
        this.rewardNote = rewardNote;
        this.value = val;
    }

    // Username setter
    public void setUsername(String username) {
        this.username = username;
    }

    // Username getter
    public String getUsername() {
        return this.username;
    }

    // Set name
    public void setName(String s) {
        this.name = s;
    }

    // Get name
    public String getName() {
        return this.name;
    }

    // Date Setter
    public void setDate(String d) {
        this.date = d;
    }

    // Date getter
    public String getDate() {
        return this.date;
    }

    // Reward Note Setter
    public void setRewardNote(String r) {
        this.rewardNote = r;
    }

    // Reward Note Getter
    public String getRewardNote() {
        return this.rewardNote;
    }

    // Value Setter
    public void setValue(int v) {
        this.value = v;
    }

    // Value Getter
    public int getValue() {
        return this.value;
    }
}
