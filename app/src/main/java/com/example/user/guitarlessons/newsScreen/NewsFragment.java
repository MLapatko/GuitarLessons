package com.example.user.guitarlessons.newsScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.NewsManager;

import java.util.List;

import me.toptas.rssconverter.RssItem;

/**
 * Created by user on 07.03.2018.
 */

public class NewsFragment extends BaseFragment {
    public static NewsFragment newInstance() {

        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.news_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getNews();
    }

    public void getNews() {
        NewsManager.getInstance().getNews("feed/", new NewsManager.NewsListener() {
            @Override
            public void onSuccess(List<RssItem> item) {
                Log.d("mylog",item.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("mylog",e.toString());
            }
        });
    }
}
