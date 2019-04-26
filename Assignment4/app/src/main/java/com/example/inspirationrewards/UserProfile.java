package com.example.inspirationrewards;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserProfile implements Serializable {
    // First name
    private String firstName;

    // Last name
    private String lastName;

    // Username
    private String username;

    // Department
    private String department;

    // Story
    private String story;

    // Position
    private String position;

    // Password
    private String password;

    // Award Points
    private int pointsToAward;

    // Admin status
    private boolean admin;

    // Profile Image is a Bitmap
    private byte[] profileImage;

    // Location
    private String location;

    // List of the user's rewards
    List<Reward> myRewards;

    public UserProfile(String firstName, String lastName, String username, String department, String story, String position, String password, int points, boolean isAdmin, byte[] profileImage, String location, List<Reward> rewardsList) {
        this.firstName =  firstName;
        this.lastName = lastName;
        this.username = username;
        this.department = department;
        this.story = story;
        this.position = position;
        this.password = password;
        this.pointsToAward = points;
        this.admin = isAdmin;
        this.profileImage = profileImage;
        this.location = location;
        this.myRewards = rewardsList;
    }

    // Set first name
    public void setFirstName(String s) {
        this.firstName = s;
    }

    // Get first name
    public String getFirstName() {
        return this.firstName;
    }

    // Set last name
    public void setLastName(String s) {
        this.lastName = s;
    }

    // Get last name
    public String getLastName() {
        return this.lastName;
    }

    // Get username
    public String getUsername() {
        return this.username;
    }

    // Set department
    public void setDepartment(String s) {
        this.department = s;
    }

    // Get department
    public String getDepartment() {
        return this.department;
    }

    // Set story
    public void setStory(String s) {
        this.story = s;
    }

    // Get story
    public String getStory() {
        return this.story;
    }

    // Set position
    public void setPosition(String s) {
        this.position = s;
    }

    // Get position
    public String getPosition() {
        return this.position;
    }

    // Set password
    public void setPassword(String s) {
        this.password = s;
    }

    // Get password
    public String getPassword() {
        return this.password;
    }

    // Set number of reward points
    public void setPointsToAward(int points) {
        this.pointsToAward = points;
    }

    // Get reward points
    public int getPointsToAward() {
        return this.pointsToAward;
    }

    // Set admin status
    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
    }

    // Get admin status
    public boolean getAdmin() {
        return this.admin;
    }

    // Set profile image
    public void setProfileImage(byte[] image) {
        this.profileImage = image;
    }

    // Get profile image
    public byte[] getProfileImage() {
        return this.profileImage;
    }

    // Set location
    public void setLocation(String s) {
        this.location = s;
    }

    // Get location
    public String getLocation() {
        return this.location;
    }

    // Set rewards list
    public void setMyRewards(List<Reward> myRewards) {
        this.myRewards = myRewards;
    }

    // Get rewards list
    public List<Reward> getMyRewards() {
        return this.myRewards;
    }

}
