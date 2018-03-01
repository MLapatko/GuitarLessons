package com.example.user.guitarlessons.model;

import android.os.Parcel;
import android.os.Parcelable;

import weborb.service.MapToProperty;

/**
 * Created by user on 20.02.2018.
 */

public class Song extends BaseModel implements Parcelable {

    @MapToProperty(property = "title")
    private String title;
    @MapToProperty(property = "author")
    private String author;
    @MapToProperty(property = "genreId")
    private String genreId;
    @MapToProperty(property = "videoUrl")
    private String videoUrl;
    @MapToProperty(property = "chords")
    private String chords;
    @MapToProperty(property = "notes")
    private String notes;
    @MapToProperty(property = "tabs")
    private String tabs;

    public Song() {
    }

    protected Song(Parcel in) {
        title = in.readString();
        author = in.readString();
        genreId = in.readString();
        videoUrl = in.readString();
        chords = in.readString();
        notes = in.readString();
        tabs = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(genreId);
        dest.writeString(videoUrl);
        dest.writeString(chords);
        dest.writeString(notes);
        dest.writeString(tabs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int getType() {
        return SONG_TYPE;
    }

    public String getTabs() {
        return tabs;
    }

    public String getChords() {
        return chords;
    }

    public void setChords(String chords) {
        this.chords = chords;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getGenreId() {
        return genreId;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "Song{" +
                "objectId='" + objectId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genreId='" + genreId + '\'' +
                '}';
    }
}
