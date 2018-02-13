package com.example.user.guitarlessons;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.example.user.guitarlessons.model.Lesson;
import com.example.user.guitarlessons.model.User;

import java.util.List;

public class MainActivity extends BaseActivity implements  View.OnClickListener{
    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
    Button logOutButton;
    final static String TAG="mylog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logOutButton=findViewById(R.id.log_out);
        logOutButton.setOnClickListener(this);

        Backendless.Data.mapTableToClass("Lessons", Lesson.class);
        Backendless.Data.mapTableToClass("BackendlessUser", User.class);
        Log.d(TAG,"MainActivity oncreate");
        //getLessons();
        //updateUserLessons();
    }

    @Override
    protected int getToolBarId() {
        return R.id.tool_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void updateUserLessons() {

        DataQueryBuilder queryBuilder=DataQueryBuilder.create();
        queryBuilder.setWhereClause("email='www@tut.by'");
        Backendless.Data.of(User.class).find(queryBuilder, new AsyncCallback<List<User>>() {
            @Override
            public void handleResponse(List<User> response) {
                for (User user:response
                        ) {
                    Log.d(TAG,"view lessons"+user.getViewLessons().toString());
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

    }


    private void getLessons() {
        DataQueryBuilder queryBuilder=DataQueryBuilder.create();
        queryBuilder.setWhereClause("objectId in (Users[email='www@tut.by'].favorite.objectId)");
        Backendless.Data.of(Lesson.class).find(queryBuilder, new AsyncCallback<List<Lesson>>() {
            @Override
            public void handleResponse(List<Lesson> response) {
                Log.d(TAG,"result query"+response.toString());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(TAG,"error while getting data "+ fault.getCode());
            }
        });
        DataQueryBuilder queryBuilderUsers=DataQueryBuilder.create();
        Backendless.Data.of(User.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_out:
                logOut();
                break;
        }
    }

    private void logOut() {
        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                Toast.makeText(MainActivity.this,"log out successfully",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG,"log out successfully");
                goToLoginActivity();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(TAG,"log out problems " +fault.getCode());
            }
        });
    }

    private void goToLoginActivity() {
        LogInActivity.start(this);
        finish();
    }
}
