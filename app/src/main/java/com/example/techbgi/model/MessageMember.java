package com.example.techbgi.model;

public class MessageMember {
    String message,time,date,type,senderuid,receiveruid;
    long timeStamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageMember() {
    }
    public MessageMember(String message,String senderUID,long timeStamp){
        this.message = message;
        this.senderuid = senderUID;
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public MessageMember(String message, String time, String date, String type, String senderuid, String receiveruid) {
        this.message = message;
        this.time = time;
        this.date = date;
        this.type = type;
        this.senderuid = senderuid;
        this.receiveruid = receiveruid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderuid() {
        return senderuid;
    }

    public void setSenderuid(String senderuid) {
        this.senderuid = senderuid;
    }

    public String getReceiveruid() {
        return receiveruid;
    }

    public void setReceiveruid(String receiveruid) {
        this.receiveruid = receiveruid;
    }

}
