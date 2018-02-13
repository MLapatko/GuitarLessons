package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by User on 12.02.2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    Toolbar mToolbar;

    protected abstract int getToolBarId();

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        initToolBar();
    }

    private void initToolBar() {
            mToolbar = findViewById(getToolBarId());
        if (mToolbar!=null) {
            setSupportActionBar(mToolbar);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void setBackButtonStatus(boolean status) {
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(status);
            getSupportActionBar().setDisplayShowHomeEnabled(status);
        }
    }
    protected void setToolbarTitle(String title){
        if (mToolbar!=null) {
            mToolbar.setTitle(title);
        }
    }
}
