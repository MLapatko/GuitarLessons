package com.example.user.guitarlessons.ui.newslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ViewSwitcher;

import com.example.user.guitarlessons.base.BaseFragment;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.NewsManager;
import com.example.user.guitarlessons.model.NewsItem;

import java.util.List;

/**
 * Created by user on 07.03.2018.
 */

public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static NewsFragment newInstance() {

        Bundle args = new Bundle();

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static final String BASE_URL = "http://celebrity.moscow";
    private static final String PATH = "/feed/";
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewSwitcher mViewSwitcher;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.news_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.news_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NewsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mViewSwitcher = view.findViewById(R.id.view_switcher);
        mViewSwitcher.setDisplayedChild(1);
        onRefresh();
    }

    public void getNews() {
        NewsManager.getInstance().getNews(PATH, BASE_URL, new NewsManager.NewsListener() {
            @Override
            public void onSuccess(List<NewsItem> item) {
                if (item.size() > 0) {
                    mAdapter.setList(item);
                    mAdapter.notifyDataSetChanged();
                }
                mViewSwitcher.setDisplayedChild(1);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("mylog", e.toString());
                mSwipeRefreshLayout.setRefreshing(false);
                mViewSwitcher.setDisplayedChild(0);
            }
        });
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getNews();
    }
}
