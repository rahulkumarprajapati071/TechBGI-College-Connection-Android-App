package com.example.techbgi.model;

public class FacultyModel {
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String collegeId;
    private String password;

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String rollNumber) {
        this.collegeId = rollNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FacultyModel(){}

    public FacultyModel(String firstName, String lastName,String mobileNumber, String rollNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.collegeId = rollNumber;
        this.password = password;
    }
}
