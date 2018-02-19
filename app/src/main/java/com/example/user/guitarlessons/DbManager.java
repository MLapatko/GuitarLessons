package com.example.user.guitarlessons;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.user.guitarlessons.model.Lesson;
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
    }

    public static final String LESSONS = "Lessons";

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

    public void getAllLessons(final DbListener<List<Lesson>> listener) {
        Backendless.Data.of(Lesson.class).find(DataQueryBuilder.create(),
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
