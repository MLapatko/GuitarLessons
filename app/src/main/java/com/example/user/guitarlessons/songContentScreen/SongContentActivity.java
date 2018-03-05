package com.example.user.guitarlessons.songContentScreen;

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
import com.example.user.guitarlessons.model.Song;

/**
 * Created by user on 03.03.2018.
 */

public class SongContentActivity extends BaseActivity {

    public static void start(Context context, String songId, String genreTitle) {
        Intent starter = new Intent(context, SongContentActivity.class);
        starter.putExtra(SONG_ID, songId);
        starter.putExtra(GENRE_TITLE, genreTitle);
        context.startActivity(starter);
    }

    public static final String SONG_ID = "sing id";
    public static final String GENRE_TITLE = "genre title";
    private ViewFlipper mViewFlipper;
    private String songId;
    private TextView name;
    private TextView author;
    private Song mSong;
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBackButtonStatus(true);
        setToolbarTitle(getIntent().getStringExtra(GENRE_TITLE));

        mViewFlipper = findViewById(R.id.viewFlipper);

        name = findViewById(R.id.song_name);
        author = findViewById(R.id.author_name);

        mWebView = findViewById(R.id.web_view);

        songId = getIntent().getStringExtra(SONG_ID);
        getSongById(songId);
    }

    private void getSongById(String songId) {
        ApiManager.getInstance().getSongById(songId, new ApiManager.DbListener<Song>() {
            @Override
            public void onSuccess(Song response) {
                mSong = response;
                name.setText(response.getTitle());
                author.setText(response.getAuthor());
                mWebView.loadData(response.getBody(), "text/html", "UTF-8");
                mViewFlipper.setDisplayedChild(1);
            }

            @Override
            public void onError(BackendlessFault fault) {
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
                if (mSong != null) {
                    if (ApiManager.getInstance().isFavorite(songId)!=-1) {
                        deleteFromUsersLessons(mSong, item);
                    } else {
                        addToUsersLessons(mSong, item);
                    }
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.add_favorite);
        if (ApiManager.getInstance().isFavorite(songId)!=-1) {
            item.setIcon(R.drawable.ic_favorite_white);
        } else {
            item.setIcon(R.drawable.ic_favorite_gray);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private <T> void addToUsersLessons(T song, final MenuItem item) {
        ApiManager.getInstance().addToUsersLessons(song, ApiManager.COLUMN_FAVORITE_SONGS,
                new ApiManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        Toast.makeText(SongContentActivity.this,
                                getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_favorite_white);
                    }

                    @Override
                    public void onError(BackendlessFault fault) {

                    }
                });
    }

    private <T> void deleteFromUsersLessons(T song, final MenuItem item) {
        ApiManager.getInstance().deleteFromUsersLessons(song, ApiManager.COLUMN_FAVORITE_SONGS,
                new ApiManager.DbListener<Integer>() {
                    @Override
                    public void onSuccess(Integer response) {
                        Toast.makeText(SongContentActivity.this,
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
        return R.layout.song_content_activity;
    }
}
