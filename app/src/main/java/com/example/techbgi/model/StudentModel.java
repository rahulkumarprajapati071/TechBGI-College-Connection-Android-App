package com.example.techbgi.model;

public class StudentModel {
    private String firstName;
    private String lastName;
    private String semester;
    private String branch;
    private String mobileNumber;
    private String rollNumber;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StudentModel(){}

    public StudentModel(String firstName, String lastName, String semester, String branch, String mobileNumber, String rollNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.semester = semester;
        this.branch = branch;
        this.mobileNumber = mobileNumber;
        this.rollNumber = rollNumber;
        this.password = password;
    }
}
