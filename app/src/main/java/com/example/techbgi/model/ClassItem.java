package com.example.techbgi.model;

public class ClassItem {
    private String className;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    private String semester;
    private String subjectName;

    private String classId;

    public ClassItem(String classId,String semester,String className,String subjectName){
        this.classId = classId;
        this.semester = semester;
        this.className = className;
        this.subjectName = subjectName;
    }

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
