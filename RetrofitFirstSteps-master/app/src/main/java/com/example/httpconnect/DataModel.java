package com.example.httpconnect;


import com.google.gson.annotations.SerializedName;

public class DataModel {

    //that class will be a model to parse a data

    private int userId;

    private int id;

    private String title;

    private boolean completed;

    // Getters
    // CTRL + INSERT and that functions will be automatically made

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}
