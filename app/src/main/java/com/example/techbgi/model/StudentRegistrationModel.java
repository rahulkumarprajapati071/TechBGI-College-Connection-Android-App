package com.example.techbgi.model;

public class StudentRegistrationModel {
    public StudentRegistrationModel(String firstName, String lastName, String branch, String semester, String password, String rollNumber, String phoneNumber,String imageUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.branch = branch;
        this.semester = semester;
        this.password = password;
        this.rollNumber = rollNumber;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }

    public StudentRegistrationModel(){}

    String firstName;
    String lastName;
    String branch;
    String semester;
    String password;
    String rollNumber;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
