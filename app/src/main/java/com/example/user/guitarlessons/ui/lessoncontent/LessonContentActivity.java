package com.example.user.guitarlessons.ui.lessoncontent;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.backendless.exceptions.BackendlessFault;
import com.example.user.guitarlessons.base.BaseActivity;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.VideoInterface;
import com.example.user.guitarlessons.managers.ApiManager;
import com.example.user.guitarlessons.managers.ContentManager;
import com.example.user.guitarlessons.model.Lesson;

import java.util.List;

/**
 * Created by user on 02.03.2018.
 */

public class LessonContentActivity extends BaseActivity implements VideoInterface {
    public static void start(Context context, String idLesson, String courseTitle) {
        Intent starter = new Intent(context, LessonContentActivity.class);
        starter.putExtra(LESSON_ID, idLesson);
        starter.putExtra(COURSE_TITLE, courseTitle);
        context.startActivity(starter);
    }

    public static final String LESSON_ID = "lesson id";
    public static final String COURSE_TITLE = "course title";
    private TextView mLessonTitle;
    private String lessonId;
    private WebView mWebView;
    private ViewFlipper mViewFlipper;
    private Lesson mLesson;
    private ProgressBar mProgressBar;
    private boolean mVideoStatus=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBackButtonStatus(true);

        mViewFlipper = findViewById(R.id.viewFlipper);
        mViewFlipper.setDisplayedChild(0);

        mProgressBar = findViewById(R.id.toolbar_progress_bar);

        lessonId = getIntent().getStringExtra(LESSON_ID);
        setToolbarTitle(getIntent().getStringExtra(COURSE_TITLE));

        mLessonTitle = findViewById(R.id.lesson_title);

        mWebView = findViewById(R.id.web_view);
        getLesson(lessonId);
        checkData();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void getLesson(String lessonId) {
        ApiManager.getInstance().getLessonById(lessonId, new ApiManager.DbListener<Lesson>() {
            @Override
            public void onSuccess(Lesson response) {
                mProgressBar.setVisibility(View.GONE);
                mLesson = response;
                mViewFlipper.setDisplayedChild(1);
                mLessonTitle.setText(response.getTitle());
                mWebView.loadData(response.getBody(), "text/html", "UTF-8");
                if (!TextUtils.isEmpty(response.getVideoUrl())) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.youtube, YoutubeViewFragment.newInstance(response.getVideoUrl()),
                            YoutubeViewFragment.class.getSimpleName());
                    ft.commit();
                }

            }

            @Override
            public void onError(BackendlessFault e) {

                mProgressBar.setVisibility(View.GONE);
                mViewFlipper.setDisplayedChild(2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lesson_tool_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_favorite:
                if (mLesson != null) {
                    item.setEnabled(false);
                    if (ApiManager.getInstance().isFavorite(mLesson.getObjectId()) != -1) {
                        deleteFromUsersLessons(mLesson, item);
                    } else {
                        addToUsersLessons(mLesson, item);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mVideoStatus){
         YoutubeViewFragment youtubeViewFragment= (YoutubeViewFragment) getSupportFragmentManager()
                 .findFragmentByTag(YoutubeViewFragment.class.getSimpleName());
         youtubeViewFragment.closeVideo();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.add_favorite);
        if (ApiManager.getInstance().isFavorite(lessonId) != -1) {
            item.setIcon(R.drawable.ic_favorite_white);
        } else {
            item.setIcon(R.drawable.ic_favorite_gray);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private <T> void addToUsersLessons(T lesson, final MenuItem item) {
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().addToUsersLessons(lesson, ApiManager.COLUMN_FAVORITE_LESSONS,
                new ApiManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {

                        mProgressBar.setVisibility(View.GONE);
                        item.setEnabled(true);
                        Toast.makeText(LessonContentActivity.this,
                                getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_favorite_white);
                    }

                    @Override
                    public void onError(BackendlessFault fault) {
                        Log.e("mylog", fault.getMessage());

                        mProgressBar.setVisibility(View.GONE);
                        item.setEnabled(true);
                        Toast.makeText(LessonContentActivity.this,
                                getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private <T> void deleteFromUsersLessons(T lesson, final MenuItem item) {
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.getInstance().deleteFromUsersLessons(lesson, ApiManager.COLUMN_FAVORITE_LESSONS,
                new ApiManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {

                        mProgressBar.setVisibility(View.GONE);
                        item.setEnabled(true);
                        Toast.makeText(LessonContentActivity.this,
                                getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_favorite_gray);
                    }

                    @Override
                    public void onError(BackendlessFault fault) {
                        Log.e("mylog", fault.getMessage());

                        mProgressBar.setVisibility(View.GONE);
                        item.setEnabled(true);
                        Toast.makeText(LessonContentActivity.this,
                                getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void checkData() {
        if (ApiManager.getInstance().getFavorite().isEmpty()) {
            ContentManager.getInstance().getFavorite(new ContentManager.ContentListener<List<Object>>() {
                @Override
                public void onSuccess(List<Object> response) {

                }

                @Override
                public void onError(Throwable e) {

                }
            });
        }
    }

    @Override
    protected int getToolBarId() {
        return R.id.tool_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.lesson_content;
    }

    @Override
    public void isFullScreen(boolean status) {
        mVideoStatus=status;
    }
}
