package com.example.user.guitarlessons.application;

import android.app.Application;

import com.backendless.Backendless;
import com.example.user.guitarlessons.managers.Defaults;

/**
 * Created by User on 12.02.2018.
 */

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(App.getInstance(), Defaults.APPLICATION_ID, Defaults.API_KEY);
    }

    public static App getInstance() {
        return instance;
    }
}
