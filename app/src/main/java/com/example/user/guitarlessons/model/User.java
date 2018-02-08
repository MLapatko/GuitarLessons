package com.example.user.guitarlessons.model;

import java.util.List;

import weborb.service.MapToProperty;

/**
 * Created by user on 06.02.2018.
 */

public class User {
    @MapToProperty(property = "objectId")
    private String objectId;
    @MapToProperty(property = "name")
    private String name;
    @MapToProperty(property = "email")
    private String email;
    @MapToProperty(property = "favorite")
    private List<Lesson> favorite;
    @MapToProperty(property = "isView")
    private List<Lesson> viewLessons;

    public List<Lesson> getViewLessons() {
        return viewLessons;
    }

    public void setViewLessons(List<Lesson> viewLessons) {
        this.viewLessons = viewLessons;
    }

    @Override

    public String toString() {
        return "User{" +
                "objectId='" + objectId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", favorite=" + favorite +
                '}';
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Lesson> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<Lesson> favorite) {
        this.favorite = favorite;
    }
}
