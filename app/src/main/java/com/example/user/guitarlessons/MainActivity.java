package com.example.user.guitarlessons;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.exceptions.BackendlessFault;
import com.example.user.guitarlessons.model.Lesson;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    Button logOutButton;
    final static String TAG = "mylog";
    ViewSwitcher mViewSwitcher;
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_ISVIEW = "isView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logOutButton = findViewById(R.id.log_out);
        logOutButton.setOnClickListener(this);

        mViewSwitcher = findViewById(R.id.viewSwitcher);
        mViewSwitcher.setDisplayedChild(1);

        setBackButtonStatus(false);

        Log.d(TAG, "currentUser" + DbManager.getInstance().getUsersLessons(COLUMN_ISVIEW));
    }

    private void deleteFromUsersLessons(Lesson lesson, String columnName) {
        DbManager.getInstance().deleteFromUsersLessons(lesson, columnName,
                new DbManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                    }

                    @Override
                    public void onError(BackendlessFault fault) {

                    }
                });
    }

    private void addToUsersLessons(Lesson lesson, String columnName) {
        DbManager.getInstance().addToUsersLessons(lesson, columnName,
                new DbManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        Log.d(TAG, "Added successfully");
                    }

                    @Override
                    public void onError(BackendlessFault fault) {

                    }
                });
    }

    private void getAllLessons() {
        DbManager.getInstance().getAllLessons(new DbManager.DbListener<List<Lesson>>() {
            @Override
            public void onSuccess(List<Lesson> response) {
            }

            @Override
            public void onError(BackendlessFault fault) {

            }
        });
    }

    @Override
    protected int getToolBarId() {
        return R.id.tool_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_out:
                logOut();
                break;
        }
    }

    private void logOut() {
        mViewSwitcher.setDisplayedChild(0);
        UserAuthManager.getInstance().logOut(new UserAuthManager.AuthListener<Void>() {
            @Override
            public void onSuccess(Void response) {
                goToLoginActivity();
            }

            @Override
            public void onError(String massage, String errorCode) {
                Toast.makeText(MainActivity.this, massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToLoginActivity() {
        LogInActivity.start(this);
        finish();
    }
}
