package com.example.newsgateway;

public class NewsSource {

    private String Id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;

    public NewsSource(String Id, String name, String description, String url, String category, String language, String country) {
        this.Id = Id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.language = language;
        this.country = country;
    }

    public String getId() {
        return this.Id;
    }

    public void setId(String s) {
        this.Id = s;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String s) {
        this.description = s;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String s) {
        this.url = s;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String s) {
        this.category = s;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String s) {
        this.language = s;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String s) {
        this.country = s;
    }
}
