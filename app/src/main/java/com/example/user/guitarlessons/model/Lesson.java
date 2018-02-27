package com.example.user.guitarlessons.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.user.guitarlessons.ModelType;

import weborb.service.MapToProperty;

/**
 * Created by user on 06.02.2018.
 */

public class Lesson implements Parcelable, ModelType {
    @MapToProperty(property = "objectId")
    String objectId;
    @MapToProperty(property = "title")
    private String title;
    @MapToProperty(property = "description")
    private String description;
    @MapToProperty(property = "videoUrl")
    private String videoUrl;
    @MapToProperty(property = "body")
    private String body;
    @MapToProperty(property = "rate")
    private int rate;
    @MapToProperty(property = "courseId")
    private String courseId;



    protected Lesson(Parcel in) {
        objectId=in.readString();
        title = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        body = in.readString();
        rate = in.readInt();
        courseId = in.readString();
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Lesson() {}

    public Lesson(String objectId, String title, String description, String videoUrl, String body, int rate) {
        this.objectId = objectId;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.body = body;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "objectId='" + objectId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", body='" + body + '\'' +
                ", rate=" + rate +
                ", courseId=" + courseId +
                '}';
    }

    @Override
    public int getType() {
        return LESSON_TYPE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(objectId);
        parcel.writeString(description);
        parcel.writeString(videoUrl);
        parcel.writeString(body);
        parcel.writeInt(rate);
        parcel.writeString(courseId);

    }
}
