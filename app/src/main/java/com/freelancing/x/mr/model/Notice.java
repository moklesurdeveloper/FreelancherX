package com.freelancing.x.mr.model;

public class Notice {
    String title,time,text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Notice(String title, String time, String text) {
        this.title = title;
        this.time = time;
        this.text = text;
    }

    public Notice() {
    }
}
