package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.user.guitarlessons.ui.auth.LogInActivity;
import com.example.user.guitarlessons.managers.UserAuthManager;

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

        login= UserAuthManager.getInstance().checkUserLogIn();
        if (login) {
            MainActivity.start(this);
        }
        else {
            LogInActivity.start(this);
        }

    }
}
