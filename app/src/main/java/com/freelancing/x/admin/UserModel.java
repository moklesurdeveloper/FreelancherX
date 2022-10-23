package com.freelancing.x.admin;

public class UserModel {
    String name,photo,role,u_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public UserModel(String name, String photo, String role, String u_id) {
        this.name = name;
        this.photo = photo;
        this.role = role;
        this.u_id = u_id;
    }

    public UserModel() {
    }
}
