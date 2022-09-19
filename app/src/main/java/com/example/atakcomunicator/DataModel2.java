package com.example.atakcomunicator;

public class DataModel2 {
    // string variables for name and job.
    private int id;
    private String title;
    private int lat;
    private int lon;

    public DataModel2(int id, String title, int lat, int lon) {
        this.id = id;
        this.title = title;
        this.lat = lat;
        this.lon = lon;
    }

    // creating getter and setter methods.
    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getlat() {
        return lat;
    }

    public void setlat(int lat) {
        this.lat = lat;
    }

    public int getlon() {
        return lon;
    }

    public void setlon(int lon) {
        this.lon = lon;
    }


}
