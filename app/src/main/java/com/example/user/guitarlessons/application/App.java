package com.example.user.guitarlessons.application;

import android.app.Application;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;
import com.example.user.guitarlessons.managers.Defaults;
import com.example.user.guitarlessons.managers.NotificationManager;
import com.example.user.guitarlessons.settingsScreen.SettingsHelper;

/**
 * Created by User on 12.02.2018.
 */

public class App extends Application {

    private static App instance;

    private static String TAG="mylog";

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(App.getInstance(), Defaults.APPLICATION_ID, Defaults.API_KEY);
        getDiviceRegistration();
        SettingsHelper.setLanguage(this,SettingsHelper.getLanguage(this));

    }

    private void getDiviceRegistration() {
        NotificationManager.getInstance().getDeviceRegistration(new NotificationManager.NotificationListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"device registered");
            }

            @Override
            public void onError(BackendlessFault fault) {
                Log.e(TAG,fault.toString());
            }
        });
    }






}
