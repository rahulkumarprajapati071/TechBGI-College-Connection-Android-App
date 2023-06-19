package com.example.techbgi.model;

public class ClassItem {
    private String className;
    private String uid;

    public ClassItem(String className, String uid, String semester, String subjectName, String classId) {
        this.className = className;
        this.uid = uid;
        this.semester = semester;
        this.subjectName = subjectName;
        this.classId = classId;
    }

    public String getUid() {
        return uid;
    }

    public ClassItem(String className, String semester, String subjectName, String uid) {
        this.className = className;
        this.uid = uid;
        this.semester = semester;
        this.subjectName = subjectName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    private String semester;
    private String subjectName;

    private String classId;

    public ClassItem(){}

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public ClassItem(String semester,String className, String subjectName) {
        this.semester = semester;
        this.className = className;
        this.subjectName = subjectName;
    }public ClassItem(String className, String subjectName) {
        this.className = className;
        this.subjectName = subjectName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
