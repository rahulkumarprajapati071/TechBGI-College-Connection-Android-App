package com.example.techbgi.model;

public class AttendanceViewModel {
    String studentName;
    String status;
    String date;

    public AttendanceViewModel() {
    }

    public String getStatus() {
        return status;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AttendanceViewModel(String date, String status, String studentName) {
        this.studentName = studentName;
        this.status = status;
        this.date = date;
    }
}
