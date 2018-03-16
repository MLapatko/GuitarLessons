package com.example.user.guitarlessons.model;

import com.example.user.guitarlessons.ModelType;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

import weborb.service.MapToProperty;

/**
 * Created by user on 20.02.2018.
 */

public class Genre extends ExpandableGroup<Song> implements ModelType {

    @MapToProperty(property = "objectId")
    String objectId;
    @MapToProperty(property = "name")
    private String title;
    private List<Song> songs = new ArrayList<>();


    public Genre() {
        super("", new ArrayList<Song>());
    }

    public Genre(String title, List<Song> items) {
        super(title, items);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<Song> getSongs() {
        return songs;
    }

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

    public void setSongs(List<Song>songs) {
        if (songs != null) {
            this.songs.clear();
            this.songs.addAll(songs);
            if (getItems() != null) {
                getItems().clear();
                getItems().addAll(songs);
            }
        }
    }
}
