package com.example.techbgi.model;

public class FacultyRegistrationModel {
    public FacultyRegistrationModel(String firstName, String lastName, String password, String collegeId, String phoneNumber,String imageUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.collegeId = collegeId;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }

    public FacultyRegistrationModel(){}

    String firstName;
    String lastName;

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    String password;
    String collegeId;
    String phoneNumber;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    String imageUri;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
