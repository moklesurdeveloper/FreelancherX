package com.freelancing.x.mr.model;

public class LeaderModel {
    String name,profile_image;
    int quiz_point;
    public LeaderModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public int getQuiz_point() {
        return quiz_point;
    }

    public void setQuiz_point(int quiz_point) {
        this.quiz_point = quiz_point;
    }

    public LeaderModel(String name, String profile_image, int quiz_point) {
        this.name = name;
        this.profile_image = profile_image;
        this.quiz_point = quiz_point;
    }
}
