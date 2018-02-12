package com.example.user.guitarlessons;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;

/**
 * Created by user on 05.02.2018.
 */

public class LogInActivity extends BaseActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, LogInActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }
    final String TAG="mylog";
    final int LOGIN_FRAGMENT=1;
    final int CREATE_USER_FRAGMENT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);


        putFragments(LOGIN_FRAGMENT,R.id.content_main);
    }

    @Override
    protected int getToolBarId() {
        return R.id.tool_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

}
