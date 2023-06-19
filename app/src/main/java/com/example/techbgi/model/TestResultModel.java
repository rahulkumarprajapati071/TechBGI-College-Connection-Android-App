package com.example.techbgi.model;

public class TestResultModel {
    private String studentName;
    private String rollNumber;
    private int totalMarks;
    private int obtainedMarks;

    public TestResultModel(String studentName, String rollNumber, int totalMarks, int obtainedMarks) {
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.totalMarks = totalMarks;
        this.obtainedMarks = obtainedMarks;
    }

    // Getters and setters
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public int getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(int obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }
}
