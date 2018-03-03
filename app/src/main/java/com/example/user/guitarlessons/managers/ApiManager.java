package com.example.user.guitarlessons.managers;

import android.text.TextUtils;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.user.guitarlessons.model.Course;
import com.example.user.guitarlessons.model.Genre;
import com.example.user.guitarlessons.model.Lesson;
import com.example.user.guitarlessons.model.LessonDetails;
import com.example.user.guitarlessons.model.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 16.02.2018.
 */

public class ApiManager {
    private static ApiManager instance;

    public static ApiManager getInstance() {
        if (instance == null) {
            return instance = new ApiManager();
        }

        return instance;
    }

    private ApiManager() {
        Backendless.Data.mapTableToClass(LESSONS_DETAILS, LessonDetails.class);
        Backendless.Data.mapTableToClass(LESSONS, Lesson.class);
        Backendless.Data.mapTableToClass(COURSES_TABLE, Course.class);
        Backendless.Data.mapTableToClass(SONGS_TABLE, Song.class);
        Backendless.Data.mapTableToClass(GENRES_TABLE, Genre.class);
    }

    public static final String LESSONS = "Lessons";
    public static final String LESSONS_DETAILS = "Lessons_details";
    public static final String COURSES_TABLE = "Courses";
    public static final String GENRES_TABLE = "Genres";
    public static final String SONGS_TABLE = "Songs";
    public static final String COLUMN_COURSE_ID = "courseId";
    public static final String COLUMN_GENRE_ID = "genreId";
    public static final String COLUMN_FAVORITE_SONGS = "favoriteSongs";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_LESSON_ID = "lesson_id";

    private BackendlessUser mCurrentUser = UserAuthManager.getInstance().getCurrentUser();
    private List<Course> courses = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();

    public void setCourses(List<Course> courses) {
        if (courses != null) {
            this.courses.clear();
            this.courses.addAll(courses);
        }
    }

    public void setGenres(List<Genre> genres) {
        if (genres != null) {
            this.genres.clear();
            this.genres.addAll(genres);
        }
    }

    public List<Genre> getGenresList() {
        return this.genres;
    }

    public List<Course> getCoursesList() {
        return this.courses;
    }

    public BackendlessUser getmCurrentUser() {
        return mCurrentUser;
    }

    public <T> void updateCurrentUser(final DbListener listener, final T queryResponce) {
        Backendless.Persistence.of(BackendlessUser.class).findById(mCurrentUser.getObjectId(),
                new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        mCurrentUser = response;
                        if (listener != null) {
                            listener.onSuccess(queryResponce);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (listener != null) {
                            listener.onError(fault);
                        }
                    }
                });
    }

    public <T> void deleteFromUsersLessons(T lesson, String columnName,
                                           final DbListener<Integer> listener) {
        List<T> lessons = new ArrayList<>();
        lessons.add(lesson);
        Backendless.Persistence.of(BackendlessUser.class).deleteRelation(mCurrentUser,
                columnName, lessons, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        if (listener != null) {
                            updateCurrentUser(listener, response);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (listener != null) {
                            listener.onError(fault);
                        }
                    }
                });
    }

    public <T> void addToUsersLessons(T lesson, String columnName,
                                      final DbListener<Integer> listener) {
        List<T> lessons = new ArrayList<>();
        lessons.add(lesson);
        Backendless.Persistence.of(BackendlessUser.class).addRelation(mCurrentUser, columnName,
                lessons, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        if (listener != null) {
                            updateCurrentUser(listener, response);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (listener != null) {
                            listener.onError(fault);
                        }
                    }
                });

    }

    public List<Lesson> getUsersLessons(String columnName) {

        Gson gson = new Gson();
        String json = gson.toJson(mCurrentUser.getProperty(columnName));
        ArrayList<Lesson> lessons = gson.fromJson(json,
                new TypeToken<ArrayList<Lesson>>() {
                }.getType());

        return lessons;
    }

    public List<Song> getFavoriteSongs() {
        Gson gson = new Gson();
        String json = gson.toJson(mCurrentUser.getProperty(COLUMN_FAVORITE_SONGS));
        ArrayList<Song> songs = gson.fromJson(json,
                new TypeToken<ArrayList<Song>>() {
                }.getType());

        return songs;
    }

    public List<Course> getCourses() {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.addSortBy("created DESC");
        return Backendless.Persistence.of(Course.class).find(queryBuilder);

    }

    public List<Genre> getGenres() {
        return Backendless.Persistence.of(Genre.class).find(DataQueryBuilder.create());
    }

    public List<Lesson> getLessonsInCourse(String courseId) {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(COLUMN_COURSE_ID + "='" + courseId + "'");
        return Backendless.Data.of(Lesson.class).find(queryBuilder);
    }

    public void getLessons(final DbListener<List<Lesson>> listener) {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        Backendless.Data.of(Lesson.class).find(queryBuilder,
                new AsyncCallback<List<Lesson>>() {
                    @Override
                    public void handleResponse(List<Lesson> response) {
                        if (listener != null) {
                            listener.onSuccess(response);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (listener != null) {
                            listener.onError(fault);
                        }
                    }
                });
    }

    public List<LessonDetails> getLessonsDetails(String idLesson) {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(COLUMN_LESSON_ID + "='" + idLesson + "'");
        return Backendless.Persistence.of(LessonDetails.class).find(queryBuilder);
    }

    public List<Song> getSongsInGenre(String genreId) {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setProperties("objectId", "title", "author", "videoUrl", "chords", "tabs");
        queryBuilder.setWhereClause(COLUMN_GENRE_ID + "='" + genreId + "'");
        return Backendless.Data.of(Song.class).find(queryBuilder);
    }

    public Lesson getLessonById(String idLesson) {
        return Backendless.Persistence.of(Lesson.class).findById(idLesson);
    }

    public void getSongById(String songId, final DbListener<Song> listener) {
        Backendless.Persistence.of(Song.class).findById(songId, new AsyncCallback<Song>() {
            @Override
            public void handleResponse(Song response) {
                if (listener != null) {
                    listener.onSuccess(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener != null) {
                    listener.onError(fault);
                }
            }
        });
    }

    public boolean isFavoriteLesson(String lessonId) {
        for (Lesson lesson : getUsersLessons(COLUMN_FAVORITE)) {
            if (TextUtils.equals(lesson.getObjectId(), lessonId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFavoriteSong(String songId) {
        for (Song song : getFavoriteSongs()) {
            if (TextUtils.equals(song.getObjectId(), songId)) {
                return true;
            }
        }
        return false;
    }


    public interface DbListener<T> {
        void onSuccess(T response);

        void onError(BackendlessFault fault);
    }
}
