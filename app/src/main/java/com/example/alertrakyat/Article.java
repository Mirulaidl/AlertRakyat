package com.example.alertrakyat;

public class Article {
    private String tag;
    private String title;
    private String description;
    private String url;

    public Article(String tag, String title, String description, String url) {
        this.tag = tag;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getTag() { return tag; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
}

