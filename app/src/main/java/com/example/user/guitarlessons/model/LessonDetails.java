package com.example.user.guitarlessons.model;

import weborb.service.MapToProperty;

/**
 * Created by user on 02.03.2018.
 */

public class LessonDetails{
    @MapToProperty(property = "description")
    private String description;
    @MapToProperty(property = "videoUrl")
    private String videoUrl;
    @MapToProperty(property = "body")
    private String body;
    @MapToProperty(property = "rate")
    private int rate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "LessonDetails{" +
                "description='" + description + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", body='" + body + '\'' +
                ", rate=" + rate +
                '}';
    }
}
