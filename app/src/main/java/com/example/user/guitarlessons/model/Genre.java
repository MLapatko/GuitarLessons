package com.example.user.guitarlessons.model;

import weborb.service.MapToProperty;

/**
 * Created by user on 20.02.2018.
 */

public class Genre extends BaseModel{

    @MapToProperty(property = "name")
    private String title;

    @Override
    public int getType() {
        return GENRE_TYPE;
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
