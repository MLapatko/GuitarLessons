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

    public void registrateUser(final String userEmail, final String userPassword, String userName,
                               final AuthListener listener) {
        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", userEmail);
        user.setProperty("name", userName);
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
        if (listener != null) {
            switch (errorCode) {
                case "3003":
                    massage = resources.getString(R.string.error_3003);
                    break;
                case "3033":
                    massage = resources.getString(R.string.error_3033);
                    break;
                case "3040":
                    massage = resources.getString(R.string.error_3040);
                    break;
                case "3006":
                    massage = resources.getString(R.string.error_3006);
                    break;

            }
            listener.onError(massage);
        }
    }

    public interface AuthListener<T> {
        void onSuccess(T response);

        void onError(String massage);

    }
}