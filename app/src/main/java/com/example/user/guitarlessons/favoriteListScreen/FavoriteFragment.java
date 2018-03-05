package com.example.user.guitarlessons.favoriteListScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ViewSwitcher;

import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.ApiManager;
import com.example.user.guitarlessons.managers.ContentManager;

import java.util.List;

/**
 * Created by user on 21.02.2018.
 */

public class FavoriteFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    public static FavoriteFragment newInstance() {

        Bundle args = new Bundle();

        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private ViewSwitcher mViewSwitcher;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FavoritesAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        mViewSwitcher = view.findViewById(R.id.viewSwitcher);
        mAdapter = new FavoritesAdapter();
        mRecyclerView.setAdapter(mAdapter);
        if (!checkData()) {
            onRefresh();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkData()) {
            onRefresh();
        }
    }

    public boolean checkData() {
        if (!ApiManager.getInstance().getFavorite().isEmpty()) {
            mAdapter.setList(ApiManager.getInstance().getFavorite());
            mAdapter.notifyDataSetChanged();
            Log.d("mylog","dats");
            return true;
        }
        return false;
    }

    private void getFavorite() {
        ContentManager.getInstance().getFavorite(new ContentManager.ContentListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> response) {

                mViewSwitcher.setDisplayedChild(0);
                if (!response.isEmpty()) {
                    mAdapter.setList(response);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mViewSwitcher.setDisplayedChild(1);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                swipeRefreshLayout.setRefreshing(false);
                mViewSwitcher.setDisplayedChild(1);
            }
        });
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.favorite_fragment;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getFavorite();
    }
}
