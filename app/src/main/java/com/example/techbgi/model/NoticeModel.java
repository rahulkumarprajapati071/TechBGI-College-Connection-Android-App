package com.example.techbgi.model;

public class NoticeModel {
    String filename;
    String fileurl;
    String username;
    String userimageurl;
    long timeStamp;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public NoticeModel(String filename, String fileurl, String username, String userimageurl, String type,long timeStamp) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.username = username;
        this.userimageurl = userimageurl;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimageurl() {
        return userimageurl;
    }

    public void setUserimageurl(String userimageurl) {
        this.userimageurl = userimageurl;
    }

    public NoticeModel() {
    }

    public NoticeModel(String filename, String fileurl, String username, String userimageurl,String type) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.username = username;
        this.userimageurl = userimageurl;
        this.type = type;
    }

}
