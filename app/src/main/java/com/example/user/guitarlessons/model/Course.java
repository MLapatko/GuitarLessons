package com.example.user.guitarlessons.model;

import weborb.service.MapToProperty;

/**
 * Created by user on 20.02.2018.
 */

public class Course {
    @MapToProperty(property = "objectId")
    private String objectId;
    @MapToProperty(property = "title")
    private String title;
    @MapToProperty(property = "author")
    private String author;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Course{" +
                "objectId='" + objectId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
