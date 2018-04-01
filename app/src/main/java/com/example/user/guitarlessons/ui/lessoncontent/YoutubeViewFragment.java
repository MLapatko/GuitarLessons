package com.example.user.guitarlessons.ui.lessoncontent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.user.guitarlessons.VideoInterface;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import static com.example.user.guitarlessons.application.App.TAG;

/**
 * Created by user on 12.03.2018.
 */

public class YoutubeViewFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener {
    public static YoutubeViewFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(VIDEO_ID, url);
        YoutubeViewFragment fragment = new YoutubeViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private final static String API_KEY = "AIzaSyBGJ1DBFTq0ald91Pf2nl_22curPL1uXmc";
    public static final String VIDEO_ID = "video_url";
    private YouTubePlayer mYouTubePlayer;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        mYouTubePlayer = youTubePlayer;
        youTubePlayer.setPlayerStateChangeListener(mPlayerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(mPlaybackEventListener);
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        if (!wasRestored) {
            youTubePlayer.cueVideo(getArguments().getString(VIDEO_ID));

            youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean b) {
                    if (getActivity() instanceof VideoInterface) {
                        ((VideoInterface) getActivity()).isFullScreen(b);
                    }
                }
            });
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.e(TAG, "initialize " + youTubeInitializationResult.toString());
    }

    private YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    private YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    public void closeVideo() {
        if (mYouTubePlayer != null) {
            mYouTubePlayer.setFullscreen(false);
        }
    }
}
