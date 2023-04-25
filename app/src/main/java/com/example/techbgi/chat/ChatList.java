package com.example.techbgi.chat;

public class ChatList {
    private String mobile;
    private String name;
    private String message;
    private String data;

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public String getTime() {
        return time;
    }

    private String time;

    public ChatList(String mobile, String name, String message, String data, String time) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;
        this.data = data;
        this.time = time;
    }

}
