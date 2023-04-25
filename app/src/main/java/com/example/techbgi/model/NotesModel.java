package com.example.techbgi.model;

public class NotesModel {
    String name,pdfSubject,pdfUrl;

    public int getNoteView() {
        return noteView;
    }

    public void setNoteView(int noteView) {
        this.noteView = noteView;
    }

    int noteView;

    public NotesModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdfSubject() {
        return pdfSubject;
    }

    public void setPdfSubject(String pdfSubject) {
        this.pdfSubject = pdfSubject;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public NotesModel(String name, String pdfSubject, String pdfUrl,int noteView) {
        this.name = name;
        this.pdfSubject = pdfSubject;
        this.pdfUrl = pdfUrl;
        this.noteView = noteView;
    }
}
