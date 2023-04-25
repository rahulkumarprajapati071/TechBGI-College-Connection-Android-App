package com.example.techbgi.model;

public class StudentItem {
    private String studentId;
    private String rollNo;
    private String studentName;
    private String status = "";
    private String classId;

    public StudentItem(){}
    public StudentItem(String studentId,String studentName,String rollNo){
        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNo = rollNo;
    }
    public StudentItem(String studentName,String rollNo) {
        this.studentName = studentName;
        this.rollNo = rollNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }


}
