package com.example.user.guitarlessons;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.guitarlessons.aboutAppScreen.AboutAppFragment;
import com.example.user.guitarlessons.auth.LogInActivity;
import com.example.user.guitarlessons.coursesListScreen.CoursesListFragment;
import com.example.user.guitarlessons.favoriteListScreen.FavoriteFragment;
import com.example.user.guitarlessons.managers.UserAuthManager;
import com.example.user.guitarlessons.metronomeScreen.MetronomeFragment;
import com.example.user.guitarlessons.newsScreen.NewsFragment;
import com.example.user.guitarlessons.settingsScreen.SettingsFragment;
import com.example.user.guitarlessons.songsListScreen.SongsListFragment;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    BottomNavigationView mBottomNavView;
    NavigationView mNavView;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mDrawerToggle;
    int mPreviousItem;
    private TextView mUerEmailTextView;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBottomNavView = findViewById(R.id.bottom_navigation);
        mBottomNavView.setOnNavigationItemSelectedListener(this);

        mDrawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavView = findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);

        header=mNavView.getHeaderView(0);

        mUerEmailTextView=header.findViewById(R.id.user_email_header);
        mUerEmailTextView.setText(UserAuthManager.getInstance().getCurrentUser().getEmail());

        onNavigationItemSelected(mBottomNavView.getMenu().findItem(R.id.courses));

        setBackButtonStatus(false);
    }

    @Override
    protected int getToolBarId() {
        return R.id.tool_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
        mPreviousItem = item.getItemId();
        return true;
    }

    private void putFragment(MenuItem item) {
        if (item.getItemId() == mPreviousItem) {
            return;
        }
        setToolbarTitle(item.getTitle().toString());
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : manager.getFragments()) {
            if (!fragment.isHidden()) {
                ft.hide(fragment);
            }
        }
        switch (item.getItemId()) {
            case R.id.courses:
                if (manager.findFragmentByTag(CoursesListFragment.class.getSimpleName()) != null) {
                    ft.show(manager.findFragmentByTag(CoursesListFragment.class.getSimpleName()));
                } else {
                    ft.add(R.id.content_main, CoursesListFragment.newInstance(),
                            CoursesListFragment.class.getSimpleName());
                }
                break;
            case R.id.songs:
                if (manager.findFragmentByTag(SongsListFragment.class.getSimpleName()) != null) {
                    ft.show(manager.findFragmentByTag(SongsListFragment.class.getSimpleName()));
                } else {
                    ft.add(R.id.content_main, SongsListFragment.newInstance(),
                            SongsListFragment.class.getSimpleName());
                }
                break;
            case R.id.favorite:
                if (manager.findFragmentByTag(FavoriteFragment.class.getSimpleName()) != null) {
                    ft.show(getSupportFragmentManager().findFragmentByTag(FavoriteFragment.class.getSimpleName()));
                } else {
                    ft.add(R.id.content_main, FavoriteFragment.newInstance(),
                            FavoriteFragment.class.getSimpleName());
                }
                break;
            case R.id.metronome:
                if (manager.findFragmentByTag(MetronomeFragment.class.getSimpleName()) != null) {
                    ft.show(manager.findFragmentByTag(MetronomeFragment.class.getSimpleName()));
                } else {
                    ft.add(R.id.content_main, MetronomeFragment.newInstance(),
                            MetronomeFragment.class.getSimpleName());
                }
                break;
            case R.id.news:
                if (manager.findFragmentByTag(NewsFragment.class.getSimpleName()) != null) {
                    ft.show(manager.findFragmentByTag(NewsFragment.class.getSimpleName()));
                } else {
                    ft.add(R.id.content_main, NewsFragment.newInstance(),
                            NewsFragment.class.getSimpleName());
                }
                break;
            case R.id.about:
                if (manager.findFragmentByTag(AboutAppFragment.class.getSimpleName()) != null) {
                    ft.show(manager.findFragmentByTag(AboutAppFragment.class.getSimpleName()));
                } else {
                    ft.add(R.id.content_main, AboutAppFragment.newInstance(),
                            AboutAppFragment.class.getSimpleName());
                }
                break;
            case R.id.settings:
                if (manager.findFragmentByTag(SettingsFragment.class.getSimpleName()) != null) {
                    ft.show(manager.findFragmentByTag(SettingsFragment.class.getSimpleName()));
                } else {
                    ft.add(R.id.content_main, SettingsFragment.newInstance(),
                            SettingsFragment.class.getSimpleName());
                }
                break;
            case R.id.exit:
                logOut();
                break;
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
