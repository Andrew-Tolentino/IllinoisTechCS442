package com.example.maginc.multinotepad;

public class Note {
    private String title;
    private String body;

    public Note(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setBody(String newBody) {
        this.body = newBody;
    }

}
