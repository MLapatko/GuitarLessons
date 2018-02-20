package com.example.user.guitarlessons;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.user.guitarlessons.model.Course;
import com.example.user.guitarlessons.model.Lesson;
import com.example.user.guitarlessons.model.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 16.02.2018.
 */

public class DbManager {
    private static DbManager instance;

    public static DbManager getInstance() {
        if (instance == null) {
            return instance = new DbManager();
        }
        return instance;
    }

    private DbManager() {
        Backendless.Data.mapTableToClass(LESSONS, Lesson.class);
        Backendless.Data.mapTableToClass(COURSES_TABLE, Course.class);
        Backendless.Data.mapTableToClass(SONGS_TABLE, Song.class);
    }

    public static final String LESSONS = "Lessons";
    public static final String COURSES_TABLE="Courses";
    public static final String SONGS_TABLE="Songs";
    public static final String COLUMN_COURSE_ID="courseId";

    private BackendlessUser mCurrentUser = UserAuthManager.getInstance().getCurrentUser();

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

    public void deleteFromUsersLessons(Lesson lesson, String columnName,
                                       final DbListener<Integer> listener) {
        List<Lesson> lessons = new ArrayList<>();
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

    public void addToUsersLessons(Lesson lesson, String columnName,
                                  final DbListener<Integer> listener) {
        List<Lesson> lessons = new ArrayList<>();
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

    public void getAllCourses(final DbListener<List<Course>> listener){
        Backendless.Persistence.of(Course.class).find(DataQueryBuilder.create(), new AsyncCallback<List<Course>>() {
            @Override
            public void handleResponse(List<Course> response) {
                if (listener!=null){
                    listener.onSuccess(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener!=null){
                    listener.onError(fault);
                }
            }
        });

    }

    public void getSongs(final DbListener<List<Song>> listener){
        Backendless.Persistence.of(Song.class).find(new AsyncCallback<List<Song>>() {
            @Override
            public void handleResponse(List<Song> response) {
                if (listener!=null){
                    listener.onSuccess(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (listener!=null){
                    listener.onError(fault);
                }
            }
        });
    }

    public void getAllLessons(String courseId, final DbListener<List<Lesson>> listener) {
        DataQueryBuilder queryBuilder=DataQueryBuilder.create();
        queryBuilder.setWhereClause(COLUMN_COURSE_ID+"='"+courseId+"'");
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


    public interface DbListener<T> {
        void onSuccess(T response);

        void onError(BackendlessFault fault);
    }
}
