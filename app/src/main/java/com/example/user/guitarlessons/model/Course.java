package com.example.user.guitarlessons.model;

import weborb.service.MapToProperty;

/**
 * Created by user on 20.02.2018.
 */

public class Course extends BaseModel{
    @MapToProperty(property = "title")
    private String title;

    @Override
    public int getType() {
        return COURSE_TYPE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Course{" +
                "objectId='" + objectId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
