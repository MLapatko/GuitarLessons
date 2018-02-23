package com.example.user.guitarlessons.model;

import weborb.service.MapToProperty;

/**
 * Created by user on 20.02.2018.
 */

public class Genre {
    @MapToProperty(property = "objectId")
    private String objectId;
    @MapToProperty(property = "name")
    private String title;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "objectId='" + objectId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
