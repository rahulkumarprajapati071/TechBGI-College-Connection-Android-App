package com.example.techbgi.model;

public class All_UserMmber {
    String name;
    String uid;
    String url;
    boolean online;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public All_UserMmber(String name, String uid, String url, String mobileNumber) {
        this.name = name;
        this.uid = uid;
        this.url = url;
        this.mobileNumber = mobileNumber;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    String mobileNumber;

    public All_UserMmber(String name, String uid, String url) {
        this.name = name;
        this.uid = uid;
        this.url = url;
    }

    public All_UserMmber() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
