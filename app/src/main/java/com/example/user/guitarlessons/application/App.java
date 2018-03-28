package com.example.user.guitarlessons.application;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.exceptions.BackendlessFault;
import com.example.user.guitarlessons.managers.Defaults;
import com.example.user.guitarlessons.managers.NotificationManager;
import com.example.user.guitarlessons.ui.settings.SettingsHelper;

import java.util.Locale;

/**
 * Created by User on 12.02.2018.
 */

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static final String TAG = "mylog";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(App.getInstance(), Defaults.APPLICATION_ID, Defaults.API_KEY);

        getDiviceRegistration();

        String currentLanguage = SettingsHelper.getLanguage(this);
        if (!TextUtils.equals(currentLanguage, Locale.getDefault().toLanguageTag())) {
            SettingsHelper.setLanguage(this, currentLanguage);
        }

    }

    private void getDiviceRegistration() {
        NotificationManager.getInstance().getDeviceRegistration(new NotificationManager.NotificationListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "device registered");
            }

            @Override
            public void onError(BackendlessFault fault) {
                Log.e(TAG, fault.toString());
            }
        });
    }
}
