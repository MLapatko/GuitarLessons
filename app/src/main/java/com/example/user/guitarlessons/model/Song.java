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
    @MapToProperty(property = "body")
    private String body;
    @MapToProperty(property = "genreId")
    private String genreId;

    public Song(){}
    protected Song(Parcel in) {
        title = in.readString();
        author = in.readString();
        body = in.readString();
        genreId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(body);
        dest.writeString(genreId);
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

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    @Override
    public int getType() {
        return SONG_TYPE;
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
