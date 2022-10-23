package com.freelancing.x.mr;

public class CommentModel {
    String name;
    String profile;
    String text;
    String key;
    String id;

    public CommentModel() {
    }

    public CommentModel(String name, String profile, String text, String key, String id) {
        this.name = name;
        this.profile = profile;
        this.text = text;
        this.key = key;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}