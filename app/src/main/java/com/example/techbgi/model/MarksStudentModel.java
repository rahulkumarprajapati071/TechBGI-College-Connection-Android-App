package com.example.techbgi.model;

public class MarksStudentModel {
    public MarksStudentModel(String rollNo, String studentName, String marks,String studentId) {
        this.rollNo = rollNo;
        this.studentName = studentName;
        this.marks = marks;
        this.studentId = studentId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public MarksStudentModel() {
    }

    String rollNo;
    String studentName;
    String marks;


    String studentId;

}
