package com.example.user.guitarlessons.model;

import java.util.List;

import weborb.service.MapToProperty;

/**
 * Created by user on 03.03.2018.
 */

public class UsersLessons {
    @MapToProperty(property = "objectId")
    private String objectId;
    @MapToProperty(property = "viewedLessons")
    private List<Lesson> viewedLessons;
    @MapToProperty(property = "favoriteSongs")
    private List<Song> favoriteSongs;
    @MapToProperty(property = "favoriteLessons")
    private List<Lesson> favoriteLessons;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<Lesson> getViewedLessons() {
        return viewedLessons;
    }

    public void setViewedLessons(List<Lesson> viewedLessons) {
        this.viewedLessons = viewedLessons;
    }

    public List<Song> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void setFavoriteSongs(List<Song> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
    }

    public List<Lesson> getFavoriteLessons() {
        return favoriteLessons;
    }

    public void setFavoriteLessons(List<Lesson> favoriteLessons) {
        this.favoriteLessons = favoriteLessons;
    }

    public <T> void deleteFromFavoriteLessons(T lesson) {
        if (lesson!=null){
            favoriteLessons.remove(lesson);
        }
    }

    @Override
    public String toString() {
        return "UsersLessons{" +
                "objectId='" + objectId + '\'' +
                ", viewedLessons=" + viewedLessons +
                ", favoriteSongs=" + favoriteSongs +
                ", favoriteLessons=" + favoriteLessons +
                '}';
    }
}
