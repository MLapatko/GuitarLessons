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

public class LogInActivity extends BaseActivity implements FragmentsInterface{
    public static void start(Context context) {
        Intent starter = new Intent(context, LogInActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(starter);
    }
    final String TAG="mylog";
    final int LOGIN_FRAGMENT=1;
    final int CREATE_USER_FRAGMENT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        putFragments(LOGIN_FRAGMENT);
    }


    @Override
    protected int getToolBarId() {
        return R.id.tool_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public void putFragments(int type) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        Fragment fragment;
        switch (type) {
            case LOGIN_FRAGMENT:
                fragment = LoginFragment.newInstance();
                ft.replace(R.id.content_main, fragment);
                setBackButtonStatus(false);
                setToolbarTitle(getResources().getString(R.string.log_in));
                break;
            case CREATE_USER_FRAGMENT:
                fragment = CreateAccountFragment.newInstance();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(CreateAccountFragment.class.getSimpleName());
                setBackButtonStatus(true);
                setToolbarTitle(getResources().getString(R.string.create_account));
                break;
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setBackButtonStatus(false);
        setToolbarTitle(getResources().getString(R.string.log_in));
    }
}
