package com.example.user.guitarlessons;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

/**
 * Created by user on 05.02.2018.
 */

public class LogInActivity extends BaseActivity implements FragmentsInterface {
    public static void start(Context context) {
        Intent starter = new Intent(context, LogInActivity.class);
        context.startActivity(starter);
    }

    final String TAG = "mylog";
    final int LOGIN_FRAGMENT = 1;
    final int CREATE_USER_FRAGMENT = 2;
    int mCurrentType;

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
        mCurrentType = type;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        switch (type) {
            case LOGIN_FRAGMENT:
                mCurrentType = LOGIN_FRAGMENT;
                ft.replace(R.id.content_main, LoginFragment.newInstance());
                setBackButtonStatus(false);
                setToolbarTitle(getString(R.string.log_in));
                break;
            case CREATE_USER_FRAGMENT:
                mCurrentType = CREATE_USER_FRAGMENT;
                ft.replace(R.id.content_main, CreateAccountFragment.newInstance());
                ft.addToBackStack(CreateAccountFragment.class.getSimpleName());
                setBackButtonStatus(true);
                setToolbarTitle(getString(R.string.create_account));
                break;
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switch (mCurrentType) {
            case CREATE_USER_FRAGMENT:
                mCurrentType = LOGIN_FRAGMENT;
                setBackButtonStatus(false);
                setToolbarTitle(getString(R.string.log_in));
                break;
            default:
                super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
