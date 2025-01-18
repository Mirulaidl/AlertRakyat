package com.example.alertrakyat;

public class Report {
    private String id;
    private String name;
    private String date;
    private String description;

    // Default constructor for Firebase
    public Report() {}

    // Constructor
    public Report(String name, String date, String description) {
        this.name = name;
        this.date = date;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}