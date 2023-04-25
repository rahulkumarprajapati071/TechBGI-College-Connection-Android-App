package com.example.techbgi.model;

public class CompanyModel {
    public CompanyModel(String name, String date, String image) {
        this.name = name;
        this.date = date;
        this.image = image;
    }

    public CompanyModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String name;
    String date;
    String image;
}
