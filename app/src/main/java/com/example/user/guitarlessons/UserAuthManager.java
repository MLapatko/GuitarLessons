package com.example.user.guitarlessons;


import android.content.res.Resources;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.example.user.guitarlessons.application.App;

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

    public static final String  ERROR_EMPTY_FIELDS="3006";
    public static final String ERROR_EMAIL="3040";
    public static final String ERROR_EMAIL_EXISTS ="3033";
    public static final String ERROR_EMAIL_OR_PASSWORD="3003";
    public static final String PASSWORD_ERROR="password";
    public static final String EMAIL_ERROR="email";


    public void logIn(final String userEmail, String userPassword, final AuthListener listener) {
        Backendless.UserService.login(userEmail, userPassword, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser user) {
                if (listener != null) {
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
        String userToken = UserTokenStorageFactory.instance().getStorage().get();
        if (userToken != null && !userToken.equals("")) {
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
        Resources resources = App.getInstance().getResources();
        String massage = resources.getString(R.string.unknown_error);
        String errorType="";
        if (listener != null) {
            switch (errorCode) {
                case ERROR_EMAIL_OR_PASSWORD:
                    massage = resources.getString(R.string.error_3003);
                    break;
                case ERROR_EMAIL_EXISTS:
                    massage = resources.getString(R.string.error_3033);
                    errorType=EMAIL_ERROR;
                    break;
                case ERROR_EMAIL:
                    massage = resources.getString(R.string.error_3040);
                    errorType=EMAIL_ERROR;
                    break;
                case ERROR_EMPTY_FIELDS:
                    massage = resources.getString(R.string.error_3006);
                    break;

            }
            listener.onError(massage,errorType);
        }
    }

    public interface AuthListener<T> {
        void onSuccess(T response);

        void onError(String massage,String errorType);

    }
}