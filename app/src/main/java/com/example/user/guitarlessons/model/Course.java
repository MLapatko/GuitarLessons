package com.example.user.guitarlessons.model;

import com.example.user.guitarlessons.ModelType;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

import weborb.service.MapToProperty;

/**
 * Created by user on 20.02.2018.
 */

public class Course extends ExpandableGroup<Lesson> implements ModelType {
    @MapToProperty(property = "objectId")
    String objectId;
    @MapToProperty(property = "title")
    private String title;
    private List<Lesson> lessons = new ArrayList<>();

    public Course() {
        super("", new ArrayList<Lesson>());
    }

    public Course(String title, List<Lesson> items) {
        super(title, items == null ? new ArrayList<Lesson>() : items);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        if (lessons != null) {
            this.lessons.addAll(lessons);
            if (getItems() != null) {
                getItems().addAll(lessons);
            }
        }
    }

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

}
