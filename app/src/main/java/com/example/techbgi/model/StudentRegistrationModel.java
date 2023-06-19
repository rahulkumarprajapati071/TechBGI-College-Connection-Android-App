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

    public StudentRegistrationModel(String firstName, String lastName, String semester, String branch, String mobileNumber, String rollNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.semester = semester;
        this.branch = branch;
        this.phoneNumber = mobileNumber;
        this.rollNumber = rollNumber;
        this.password = password;
    }

    String firstName;
    String lastName;
    String branch;
    String semester;
    String password;
    String rollNumber;
    String phoneNumber;

    String specialization;
    String email;

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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StudentRegistrationModel(String firstName, String lastName, String branch, String semester, String password, String rollNumber, String phoneNumber, String specialization, String email, String imageUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.branch = branch;
        this.semester = semester;
        this.password = password;
        this.rollNumber = rollNumber;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.email = email;
        this.imageUri = imageUri;
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
