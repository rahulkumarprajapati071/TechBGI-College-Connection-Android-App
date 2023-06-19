package com.example.techbgi.model;

public class FacultyRegistrationModel {
    public FacultyRegistrationModel(String firstName, String lastName, String password, String collegeId, String phoneNumber,String imageUri,String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.collegeId = collegeId;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
        this.uid = uid;
    }

    public FacultyRegistrationModel(String firstName, String lastName,String mobileNumber, String rollNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = mobileNumber;
        this.collegeId = rollNumber;
        this.password = password;
    }

    public FacultyRegistrationModel(){}

    String firstName;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public FacultyRegistrationModel(String firstName, String uid, String lastName, String password, String collegeId, String phoneNumber, String imageUri, String specialization, String email, String experience, String status) {
        this.firstName = firstName;
        this.uid = uid;
        this.lastName = lastName;
        this.password = password;
        this.collegeId = collegeId;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
        this.specialization = specialization;
        this.email = email;
        this.experience = experience;
        this.status = status;
    }

    String specialization;
    String email;
    String experience;
    String status;

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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
