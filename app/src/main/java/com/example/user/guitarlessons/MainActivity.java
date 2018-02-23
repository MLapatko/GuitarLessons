package com.example.user.guitarlessons;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.backendless.exceptions.BackendlessFault;
import com.example.user.guitarlessons.model.Course;
import com.example.user.guitarlessons.model.Genre;
import com.example.user.guitarlessons.model.Lesson;
import com.example.user.guitarlessons.model.Song;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    //Button logOutButton;
    BottomNavigationView mBottomNavView;
    NavigationView mNavView;
    DrawerLayout mDrawer;
    final static String TAG = "mylog";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_ISVIEW = "isView";
    public static final String COLUMN_FAVORITE_SONGS = "favoriteSongs";
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //logOutButton = findViewById(R.id.log_out);
        //logOutButton.setOnClickListener(this);

        mBottomNavView = findViewById(R.id.bottom_navigation);
        mBottomNavView.setOnNavigationItemSelectedListener(this);

        mDrawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavView = findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(mBottomNavView.getMenu().findItem(R.id.courses));

        setBackButtonStatus(false);

    }

    private void getCourses() {
        DbManager.getInstance().getCourses(new DbManager.DbListener<List<Course>>() {
            @Override
            public void onSuccess(List<Course> response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onError(BackendlessFault fault) {
                Log.d(TAG, fault.getMessage());
            }
        });
    }

    private void getGenres() {
        DbManager.getInstance().getGenres(new DbManager.DbListener<List<Genre>>() {
            @Override
            public void onSuccess(List<Genre> response) {
                Log.d(TAG, response.toString());
                getSongs(response.get(0).getObjectId());

            }

            @Override
            public void onError(BackendlessFault fault) {

            }
        });
    }

    private <T> void deleteFromUsersLessons(T lesson, String columnName) {
        DbManager.getInstance().deleteFromUsersLessons(lesson, columnName,
                new DbManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        Log.d(TAG, "delete successfully");
                    }

                    @Override
                    public void onError(BackendlessFault fault) {

                    }
                });
    }

    private <T> void addToUsersLessons(T lesson, String columnName) {
        DbManager.getInstance().addToUsersLessons(lesson, columnName,
                new DbManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        Log.d(TAG, "Added successfully");
                    }

                    @Override
                    public void onError(BackendlessFault fault) {
                        Log.d(TAG, fault.getMessage());
                    }
                });
    }

    private void getLessons(String courseId) {
        DbManager.getInstance().getLessonsInCourse(courseId, new DbManager.DbListener<List<Lesson>>() {
            @Override
            public void onSuccess(List<Lesson> response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onError(BackendlessFault fault) {
                Log.d(TAG, fault.getMessage());
            }
        });
    }

    private void getSongs(String genreId) {
        DbManager.getInstance().getSongsInGenre(genreId, new DbManager.DbListener<List<Song>>() {
            @Override
            public void onSuccess(List<Song> response) {
                Log.d(TAG, response.toString());
                addToUsersLessons(response.get(0), COLUMN_FAVORITE_SONGS);
            }

            @Override
            public void onError(BackendlessFault fault) {
                Log.d(TAG, fault.getMessage());
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
        /*switch (view.getId()) {
            case R.id.log_out:
                logOut();
                break;
        }*/
    }

    private void logOut() {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Menu bottomMenu = mBottomNavView.getMenu();
        MenuItem bottomMenuItem = bottomMenu.findItem(item.getItemId());
        if (mDrawer.isDrawerOpen(mNavView)) {
            if (bottomMenuItem != null) {

                if (!bottomMenuItem.isCheckable()) {
                    bottomMenu.setGroupCheckable(R.id.bottom, true, true);
                }

                bottomMenuItem.setChecked(true);

            } else if (bottomMenu.getItem(0).isCheckable()) {
                bottomMenu.setGroupCheckable(R.id.bottom, false, true);
            }

            mDrawer.closeDrawers();

        } else {

            if (!bottomMenuItem.isCheckable()) {
                bottomMenu.setGroupCheckable(R.id.bottom, true, true);
            }

            mNavView.getMenu().findItem(item.getItemId()).setChecked(true);
        }

        putFragment(item);

        return true;
    }

    private void putFragment(MenuItem item) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.courses:
                ft.replace(R.id.content_main, CoursesListFragment.newInstance());
                break;
            case R.id.songs:
                ft.replace(R.id.content_main, SongsListFragment.newInstance());
                break;
            case R.id.favorite:
                ft.replace(R.id.content_main, FavoriteFragment.newInstance());
                break;
            case R.id.metronome:
                ft.replace(R.id.content_main, MetronomeFragment.newInstance());
        }
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
