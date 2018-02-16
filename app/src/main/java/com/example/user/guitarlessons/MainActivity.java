package com.example.user.guitarlessons;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
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
    ViewSwitcher mViewSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logOutButton=findViewById(R.id.log_out);
        logOutButton.setOnClickListener(this);

        mViewSwitcher=findViewById(R.id.viewSwitcher);
        mViewSwitcher.setDisplayedChild(1);

        Backendless.Data.mapTableToClass("Lessons", Lesson.class);
        Backendless.Data.mapTableToClass("Users", User.class);
        Log.d(TAG,"MainActivity oncreate");
        setBackButtonStatus(false);
        //getLessons();
        //updateUserLessons();
        getAllLessons();
    }
    private void updateUserFavorits(final List<Lesson> response){
        String currentUserId=Backendless.UserService.loggedInUser();
         Backendless.Persistence.of(User.class)
                .findById(currentUserId, new AsyncCallback<User>() {
                    @Override
                    public void handleResponse(User user) {
                        Log.d(TAG,"user " +user.toString());
                        Log.d(TAG,"favorite "+user.toString());
                        Backendless.Data.of( User.class ).setRelation( user, "favorite", response,
                                new AsyncCallback<Integer>()
                                {
                                    @Override
                                    public void handleResponse( Integer response )
                                    {
                                        Log.i( "MYAPP", "relation has been set");
                                    }

                                    @Override
                                    public void handleFault( BackendlessFault fault )
                                    {
                                        Log.e( "MYAPP", "server reported an error - " + fault.getMessage() );
                                    }
                                } );

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.d(TAG,"fault "+fault.getCode()+" "+fault.getMessage()+" "+fault.getDetail());
                    }
                });

        /*Backendless.Persistence.of(User.class)
                .findById(Backendless.UserService.CurrentUser().getObjectId(), new AsyncCallback<User>() {
                    @Override
                    public void handleResponse(User user) {
                        Log.d(TAG,"user " +user.toString());
                        user.getFavorite().addAll(response);
                        Log.d(TAG,"favorite "+user.toString());
                        Backendless.Persistence.save(user, new AsyncCallback<User>() {
                            @Override
                            public void handleResponse(User response) {
                                Log.d(TAG,response.toString());
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Log.d(TAG,"error "+fault.getDetail());
                            }
                        });

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.d(TAG,"fault "+fault.getCode()+" "+fault.getMessage()+" "+fault.getDetail());
                    }
                });*/


    }

    private void updateUserLessons(final List<Lesson> lessons) {

        DataQueryBuilder queryBuilder=DataQueryBuilder.create();
        queryBuilder.setWhereClause("email='www@tut.by'");
        final BackendlessUser user=Backendless.UserService.CurrentUser();
        Log.d(TAG,"user"+user);
        user.setProperty("favorite",lessons.get(0));
        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.d(TAG,"update response "+response.toString());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(TAG,"error update "+fault.getCode());
            }
        });

    }

    private void getAllLessons(){
        Backendless.Data.of(Lesson.class).find(DataQueryBuilder.create(), new AsyncCallback<List<Lesson>>() {
            @Override
            public void handleResponse(List<Lesson> response) {
                Log.d(TAG,"lessons"+ response.toString());
                updateUserFavorits(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

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
        mViewSwitcher.setDisplayedChild(0);
        UserAuthManager.getInstance().logOut(new UserAuthManager.AuthListener<Void>() {
            @Override
            public void onSuccess(Void response) {
                goToLoginActivity();
            }

            @Override
            public void onError(String massage,String errorCode) {
                Toast.makeText(MainActivity.this,massage,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToLoginActivity() {
        LogInActivity.start(this);
        finish();
    }
}
