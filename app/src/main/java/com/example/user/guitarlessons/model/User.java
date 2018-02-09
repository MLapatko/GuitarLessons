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
    @MapToProperty(property = "gp_family_name")
    private String familyName;
    @MapToProperty(property = "gp_given_name")
    private String givenName;
    @MapToProperty(property = "id")
    private String id;

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


    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
