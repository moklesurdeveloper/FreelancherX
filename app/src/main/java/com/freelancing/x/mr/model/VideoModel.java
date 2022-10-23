package com.freelancing.x.mr.model;

public class VideoModel {
    String image,name,video;
    public VideoModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public VideoModel(String image, String name, String video) {
        this.image = image;
        this.name = name;
        this.video = video;
    }
}
