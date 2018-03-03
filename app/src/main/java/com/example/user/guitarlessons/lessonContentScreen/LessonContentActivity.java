package com.example.user.guitarlessons.lessonContentScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.backendless.exceptions.BackendlessFault;
import com.example.user.guitarlessons.BaseActivity;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.ApiManager;
import com.example.user.guitarlessons.managers.ContentManager;
import com.example.user.guitarlessons.model.Lesson;

/**
 * Created by user on 02.03.2018.
 */

public class LessonContentActivity extends BaseActivity {
    public static void start(Context context, String idLesson) {
        Intent starter = new Intent(context, LessonContentActivity.class);
        starter.putExtra(LESSON_ID, idLesson);
        context.startActivity(starter);
    }

    public static final String LESSON_ID = "lesson id";
    public static final String TAG = "mylog";
    private TextView mLessonTitle;
    private String lessonId;
    private WebView mWebView;
    private ViewFlipper mViewFlipper;
    private Lesson mLesson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBackButtonStatus(true);

        mViewFlipper = findViewById(R.id.viewFlipper);
        mViewFlipper.setDisplayedChild(0);

        lessonId = getIntent().getStringExtra(LESSON_ID);

        mLessonTitle = findViewById(R.id.lesson_title);

        mWebView = findViewById(R.id.web_view);
        getLesson(lessonId);

    }

    private void getLesson(String lessonId) {
        ContentManager.getInstance().getLesson(lessonId, new ContentManager.ContentListener<Lesson>() {
            @Override
            public void onSuccess(Lesson response) {
                mLesson = response;
                mViewFlipper.setDisplayedChild(1);
                mLessonTitle.setText(response.getTitle());
                mWebView.loadData(response.getDetails().getBody(), "text/html", "UTF-8");
            }

            @Override
            public void onError(Throwable e) {
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
                    if (ApiManager.getInstance().isFavoriteLesson(lessonId)) {
                        deleteFromUsersLessons(mLesson, item);
                    } else {
                        addToUsersLessons(mLesson, item);
                    }
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.add_favorite);
        if (ApiManager.getInstance().isFavoriteLesson(lessonId)) {
            item.setIcon(R.drawable.ic_favorite_white);
        } else {
            item.setIcon(R.drawable.ic_favorite_gray);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private <T> void addToUsersLessons(T lesson, final MenuItem item) {
        ApiManager.getInstance().addToUsersLessons(lesson, ApiManager.COLUMN_FAVORITE,
                new ApiManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        Toast.makeText(LessonContentActivity.this,
                                getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_favorite_white);
                    }

                    @Override
                    public void onError(BackendlessFault fault) {
                    }
                });
    }

    private <T> void deleteFromUsersLessons(T lesson, final MenuItem item) {
        ApiManager.getInstance().deleteFromUsersLessons(lesson, ApiManager.COLUMN_FAVORITE,
                new ApiManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        Toast.makeText(LessonContentActivity.this,
                                getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_favorite_gray);
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
        return R.layout.lesson_content;
    }

}
