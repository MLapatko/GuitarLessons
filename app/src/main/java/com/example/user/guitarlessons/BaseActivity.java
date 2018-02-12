package com.example.user.guitarlessons;

import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by User on 12.02.2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    final int LOGIN_FRAGMENT = 1;
    final int CREATE_USER_FRAGMENT = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        initToolBar();
    }

    private void initToolBar() {
        toolbar = findViewById(getToolBarId());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }


    protected void putFragments(int idFragment, int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        switch (idFragment) {
            case LOGIN_FRAGMENT:
                fragment = LoginFragment.newInstance();
                ft.replace(id, fragment);
                break;
            case CREATE_USER_FRAGMENT:
                fragment = CreateAccountFragment.newInstance();
                ft.replace(id, fragment);
                ft.addToBackStack(CreateAccountFragment.class.getSimpleName());
        }
        ft.commit();
    }

    protected void setBackButtonStatus(boolean status) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(status);
    }

    protected abstract int getToolBarId();

    protected abstract int getLayoutId();
}
