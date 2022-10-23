package com.freelancing.x;

public class ListModel {
    String name,image,path;

    public ListModel(String path,String name, String image) {
        this.name = name;
        this.image = image;
        this.path=path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ListModel() {
    }
}
