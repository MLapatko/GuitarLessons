package com.example.user.guitarlessons.managers;


import android.content.Context;
import android.content.SharedPreferences;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.application.App;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 13.02.2018.
 */

public class UserAuthManager {

    private static UserAuthManager instance;

    private UserAuthManager() {
    }

    public static UserAuthManager getInstance() {

        if (instance == null) {
            instance = new UserAuthManager();
        }
        return instance;
    }

    public static final String ERROR_EMPTY_FIELDS = "3006";
    public static final String ERROR_EMAIL = "3040";
    public static final String ERROR_EMAIL_EXISTS = "3033";
    public static final String ERROR_EMAIL_OR_PASSWORD = "3003";
    public static final String ERROR_FIND_USER = "3020";
    public static final String EMAIL_ERROR = "email";
    public static final String CUR_USER = "user";
    public static final String PREFERENCES = "preferences";


    private void saveUser(BackendlessUser user) {
        SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(CUR_USER, json);
        prefsEditor.commit();
    }

    public BackendlessUser getCurrentUser() {
        SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CUR_USER, "");
        return gson.fromJson(json, BackendlessUser.class);
    }

    public void restorePassword(String userEmail, final AuthListener<Void> listener) {
        Backendless.UserService.restorePassword(userEmail, new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                if (listener != null) {
                    listener.onSuccess(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                returnError(fault.getCode(), listener);
            }
        });

    }

    public void logIn(final String userEmail, String userPassword, final AuthListener listener) {
        Backendless.UserService.login(userEmail, userPassword, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser user) {
                if (listener != null) {
                    saveUser(user);
                    listener.onSuccess(user);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                returnError(fault.getCode(), listener);
            }
        }, true);
    }

    public void registrateUser(final String userEmail, final String userPassword,
                               final AuthListener listener) {
        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", userEmail);
        user.setPassword(userPassword);
        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                logIn(userEmail, userPassword, listener);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                returnError(fault.getCode(), listener);
            }
        });

    }

    public boolean checkUserLogIn() {
        if (getCurrentUser() != null) {
            return true;
        }
        return false;
    }

    public void handleAccessTokenInBackendless(String idToken, String accessToken,
                                               List<String> permissions,
                                               Map<String, String> googlePlusFieldsMapping,
                                               final AuthListener listener) {

        if (idToken != null && accessToken != null)
            Backendless.UserService.loginWithGooglePlusSdk(idToken,
                    accessToken,
                    googlePlusFieldsMapping,
                    permissions,
                    new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {
                            if (listener != null) {
                                saveUser(backendlessUser);
                                listener.onSuccess(backendlessUser);
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            returnError(backendlessFault.getCode(), listener);
                        }
                    }, true);
    }

    public void logOut(final AuthListener listener) {
        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                if (listener != null) {
                    saveUser(null);
                    ApiManager.getInstance().clearData();
                    listener.onSuccess(response);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                returnError(fault.getCode(), listener);
            }
        });
    }

    private void returnError(String errorCode, AuthListener listener) {
        App myApp = App.getInstance();
        String massage = myApp.getString(R.string.unknown_error);
        String errorType = "";
        if (listener != null) {
            switch (errorCode) {
                case ERROR_EMAIL_OR_PASSWORD:
                    massage = myApp.getString(R.string.error_3003);
                    break;
                case ERROR_EMAIL_EXISTS:
                    massage = myApp.getString(R.string.error_3033);
                    errorType = EMAIL_ERROR;
                    break;
                case ERROR_EMAIL:
                    massage = myApp.getString(R.string.error_3040);
                    errorType = EMAIL_ERROR;
                    break;
                case ERROR_EMPTY_FIELDS:
                    massage = myApp.getString(R.string.error_3006);
                    break;
                case ERROR_FIND_USER:
                    massage = myApp.getString(R.string.error_3020);
                    errorType = EMAIL_ERROR;
                    break;

            }
            listener.onError(massage, errorType);
        }
    }

    public interface AuthListener<T> {
        void onSuccess(T response);

        void onError(String massage, String errorType);

    }
}