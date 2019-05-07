package com.example.newsgateway;

import java.io.Serializable;

public class Article implements Serializable {

    private String author;

    private String title;

    private String description;

    private String url;

    private String imageUrl;

    private String date;

    private int articleNumber;

    public Article(String auth, String title, String desc, String url, String imageUrl, String date, int articleNumber) {
        this.author = auth;
        this.title = title;
        this.description = desc;
        this.url = url;
        this.imageUrl = imageUrl;
        this.date = date;
        this.articleNumber = articleNumber;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUrl() {
        return this.url;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getDate() {
        return this.date;
    }

    public int getPos() {
        return this.articleNumber;
    }

}
