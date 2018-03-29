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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.guitarlessons.base.BaseActivity;
import com.example.user.guitarlessons.ui.about.AboutAppFragment;
import com.example.user.guitarlessons.ui.auth.LogInActivity;
import com.example.user.guitarlessons.ui.courseslist.CoursesListFragment;
import com.example.user.guitarlessons.ui.favoritelist.FavoriteFragment;
import com.example.user.guitarlessons.managers.UserAuthManager;
import com.example.user.guitarlessons.ui.metronome.MetronomeFragment;
import com.example.user.guitarlessons.ui.newslist.NewsFragment;
import com.example.user.guitarlessons.ui.settings.SettingsActivity;
import com.example.user.guitarlessons.ui.songslist.SongsListFragment;
import com.example.user.guitarlessons.ui.tunings.TuningsFragment;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    private static final int CODE_SETTINGS = 2;
    private BottomNavigationView mBottomNavView;
    private NavigationView mNavView;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mUerEmailTextView;
    private View header;
    private String mPreviousFragment;


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

        header = mNavView.getHeaderView(0);

        mUerEmailTextView = header.findViewById(R.id.user_email_header);
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
        setContent(item);
        return true;
    }

    private void setContent(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, CODE_SETTINGS);
                break;
            default:
                putFragment(item);
        }
    }

    private void putFragment(MenuItem item) {

        setToolbarTitle(item.getTitle().toString());
        String tag = "";
        Fragment fragment = null;

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (!TextUtils.isEmpty(mPreviousFragment)) {
            Fragment prevFragment = manager.findFragmentByTag(mPreviousFragment);
            if (prevFragment != null) {
                ft.hide(prevFragment);
            }

        }
        switch (item.getItemId()) {
            case R.id.courses:
                tag = CoursesListFragment.class.getSimpleName();
                fragment = CoursesListFragment.newInstance();
                break;
            case R.id.songs:
                tag = SongsListFragment.class.getSimpleName();
                fragment = SongsListFragment.newInstance();
                break;
            case R.id.favorite:
                tag = FavoriteFragment.class.getSimpleName();
                fragment = FavoriteFragment.newInstance();
                break;
            case R.id.metronome:
                tag = MetronomeFragment.class.getSimpleName();
                fragment = MetronomeFragment.newInstance();
                break;
            case R.id.tunings:
                tag = TuningsFragment.class.getSimpleName();
                fragment = TuningsFragment.newInstance();
                break;
            case R.id.news:
                tag = NewsFragment.class.getSimpleName();
                fragment = NewsFragment.newInstance();
                break;
            case R.id.about:
                tag = AboutAppFragment.class.getSimpleName();
                fragment = AboutAppFragment.newInstance();
                break;
            case R.id.exit:
                logOut();
                break;
        }
        if (!TextUtils.isEmpty(tag) && manager.findFragmentByTag(tag) != null) {
            ft.show(manager.findFragmentByTag(tag));
        } else if (fragment != null) {
            ft.add(R.id.content_main, fragment, tag);
        }
        mPreviousFragment = tag;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_SETTINGS) {
                if (data.getBooleanExtra(SettingsActivity.LANGUAGE_STATUS, false)) {
                    start(this);
                    finish();
                }
            }
        }
    }
}
