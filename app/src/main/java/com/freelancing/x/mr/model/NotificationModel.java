package com.freelancing.x.mr.model;

public class NotificationModel {
    String kind, link, message, status, type,key;
Long time;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public NotificationModel(String kind, String link, String message, String status,  Long time,String type,String key ) {
        this.kind = kind;
        this.link = link;
        this.message = message;
        this.status = status;
        this.type = type;
        this.key = key;
        this.time = time;
    }

    public NotificationModel() {
    }
}