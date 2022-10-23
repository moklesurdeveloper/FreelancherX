package com.freelancing.x.mr.model;

public class newModel {
    String title,text,question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public newModel(String title, String text, String question) {
        this.title = title;
        this.text = text;
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public newModel() {
    }
}
