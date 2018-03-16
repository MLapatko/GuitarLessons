package com.example.user.guitarlessons.aboutAppScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.R;

/**
 * Created by user on 15.03.2018.
 */

public class AboutAppFragment extends BaseFragment {
    public static AboutAppFragment newInstance() {
        Bundle args = new Bundle();
        AboutAppFragment fragment = new AboutAppFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private WebView mWebView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView=view.findViewById(R.id.about_web_view);
        mWebView.loadUrl("file:///android_asset/aboutApp.html");
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.about_fragment;
    }
}
