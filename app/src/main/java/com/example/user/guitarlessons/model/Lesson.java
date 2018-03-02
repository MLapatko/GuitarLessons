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
    @MapToProperty(property = "courseId")
    private String courseId;
    LessonDetails details;


    protected Lesson(Parcel in) {
        objectId=in.readString();
        title = in.readString();
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

    public LessonDetails getDetails() {
        return details;
    }

    public void setDetails(LessonDetails details) {
        this.details = details;
    }

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
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "objectId='" + objectId + '\'' +
                ", title='" + title + '\'' +
                ", courseId='" + courseId + '\'' +
                ", details=" + details +
                '}';
    }

    @Override
    public int getType() {
        return LESSON_TYPE;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(objectId);
        parcel.writeString(title);
        parcel.writeString(courseId);

    }
}
