package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.persistence.local.UserTokenStorageFactory;

/**
 * Created by User on 12.02.2018.
 */

public class SplashActivity extends AppCompatActivity {
    final String TAG="myLog";
    private boolean login=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);

        Log.d(TAG,"onCreate splashActivity");
        isUserLogIn();

        if (login)
            MainActivity.start(this);
        else LogInActivity.start(this);

    }

    private void isUserLogIn() {
        super.onStart();
        String userToken = UserTokenStorageFactory.instance().getStorage().get();
        if (userToken != null && !userToken.equals(""))
            login=true;
    }
}
