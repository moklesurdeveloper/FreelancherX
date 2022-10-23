package com.freelancing.x;

public class VideoModel {
    String image,name;int video;
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

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public VideoModel(String image, String name, int video) {
        this.image = image;
        this.name = name;
        this.video = video;
    }
}
