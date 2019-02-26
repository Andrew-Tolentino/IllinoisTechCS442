package com.example.maginc.multinotepad;

import android.util.Log;

import java.util.Calendar;

public class Note {
    private String title;
    private String body;
    private String timeStamp;

    public Note(String title, String body, String time) {
        this.title = title;
        this.body = body;
        this.timeStamp = time;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getBody() {
        return this.body;
    }

    public String cutBody() {
        // If the size of the body is less than 80 characters display the whole body
        if (this.body.length() <= 80) {
            return this.body;
        }
        return this.body.substring(0, 80) + "...";

    }

    public void setBody(String newBody) {
        this.body = newBody;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setNewTimeStampe(String time) {
        this.timeStamp = time;
    }

}
