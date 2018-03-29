package com.example.user.guitarlessons.ui.favoritelist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewSwitcher;

import com.example.user.guitarlessons.base.BaseFragment;
import com.example.user.guitarlessons.FilterLayout;
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
    private FilterLayout mFilterLayout;

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
        mFilterLayout = view.findViewById(R.id.filterLayout);
        mFilterLayout.setListener(new FilterLayout.FilterListener() {
            @Override
            public void onAllClicked() {
                if (!ApiManager.getInstance().getFavorite().isEmpty()) {
                    mAdapter.setList(ApiManager.getInstance().getFavorite());
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    onRefresh();
                }
            }

            @Override
            public void onChordsClicked() {
                mAdapter.setList(ApiManager.getInstance().getFavoriteChordsSongs());
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onTabsClicked() {
                mAdapter.setList(ApiManager.getInstance().getFavoriteTabsSongs());
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        mFilterLayout.refreshData();
    }

    private void getFavorite() {
        ContentManager.getInstance().getFavorite(new ContentManager.ContentListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> response) {
                if (!response.isEmpty()) {
                    mFilterLayout.refreshData();
                    mViewSwitcher.setDisplayedChild(0);
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

    @Override
    public void onStop() {
        super.onStop();
        ContentManager.getInstance().stopProcess();
        swipeRefreshLayout.setRefreshing(false);

    }
}
