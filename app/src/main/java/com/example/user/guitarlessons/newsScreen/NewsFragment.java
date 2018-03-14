package com.example.user.guitarlessons.newsScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.NewsManager;
import com.example.user.guitarlessons.model.NewsItem;

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
    RecyclerView mRecyclerView;
    NewsAdapter mAdapter;
    @Override
    protected int getFragmentLayoutId() {
        return R.layout.news_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView=view.findViewById(R.id.news_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter=new NewsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        getNews();
    }

    public void getNews() {
        NewsManager.getInstance().getNews("/feed/", new NewsManager.NewsListener() {
            @Override
            public void onSuccess(List<NewsItem> item) {
                if (item.size()>0) {
                    mAdapter.setList(item);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("mylog",e.toString());
            }
        });
    }
}