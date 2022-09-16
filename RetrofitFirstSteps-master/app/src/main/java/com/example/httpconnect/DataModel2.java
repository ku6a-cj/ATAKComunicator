package com.example.httpconnect;

public class DataModel2 {
    // string variables for name and job.
    private String name;
    private String job;

    public DataModel2(String name, String job) {
        this.name = name;
        this.job = job;
    }

    // creating getter and setter methods.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
