package com.example.techbgi.model;

public class QuestionsDetailsModel {
    String date;
    String facultyName;
    String jsonQuestion;
    String subjectName;
    String timeEnd;
    String timeStart;
    String uid;
    String semester;

    public QuestionsDetailsModel(String date, String facultyName, String jsonQuestion, String subjectName, String timeEnd, String timeStart, String uid, String semester, String branch) {
        this.date = date;
        this.facultyName = facultyName;
        this.jsonQuestion = jsonQuestion;
        this.subjectName = subjectName;
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
        this.uid = uid;
        this.semester = semester;
        this.branch = branch;
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

    String branch;

    public String getDate() {
        return date;
    }

    public QuestionsDetailsModel() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getJsonQuestion() {
        return jsonQuestion;
    }

    public void setJsonQuestion(String jsonQuestion) {
        this.jsonQuestion = jsonQuestion;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public QuestionsDetailsModel(String date, String facultyName, String jsonQuestion, String subjectName, String timeStart, String timeEnd,String semester,String branch) {
        this.date = date;
        this.facultyName = facultyName;
        this.jsonQuestion = jsonQuestion;
        this.subjectName = subjectName;
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
        this.semester = semester;
        this.branch = branch;
    }
    public QuestionsDetailsModel(String date, String facultyName, String subjectName, String timeStart, String timeEnd) {
        this.date = date;
        this.facultyName = facultyName;
        this.subjectName = subjectName;
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
    }
}
