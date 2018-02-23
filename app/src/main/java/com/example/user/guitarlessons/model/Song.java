package com.example.user.guitarlessons.model;

import weborb.service.MapToProperty;

/**
 * Created by user on 20.02.2018.
 */

public class Song {
    @MapToProperty(property = "objectId")
    private String objectId;
    @MapToProperty(property = "title")
    private String title;
    @MapToProperty(property = "author")
    private String author;
    @MapToProperty(property = "body")
    private String body;
    @MapToProperty(property = "genreId")
    private String genreId;

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Song{" +
                "objectId='" + objectId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", genreId='" + genreId + '\'' +
                '}';
    }
}
