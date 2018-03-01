package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewSwitcher;

import com.example.user.guitarlessons.managers.ContentManager;
import com.example.user.guitarlessons.model.Genre;

import java.util.List;

/**
 * Created by user on 21.02.2018.
 */

public class SongsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    public static SongsListFragment newInstance() {

        Bundle args = new Bundle();

        SongsListFragment fragment = new SongsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private GenresAdapter mGenresAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewSwitcher mViewSwitcher;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.songs_list_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewSwitcher = view.findViewById(R.id.viewSwitcher);
        mViewSwitcher.setDisplayedChild(0);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (!checkData()) {
            onRefresh();
        }
    }

    private void loadGenres() {

        mViewSwitcher.setDisplayedChild(0);
        ContentManager.getInstance().loadGenres(new ContentManager.ContentListener<List<Genre>>() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (mGenresAdapter != null) {
                    mGenresAdapter.setList(genres);
                } else {
                    mGenresAdapter = new GenresAdapter(genres);
                    mRecyclerView.setAdapter(mGenresAdapter);
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                swipeRefreshLayout.setRefreshing(false);
                if (!checkData()) {
                    mViewSwitcher.setDisplayedChild(1);
                }
            }
        });
    }

    public boolean checkData() {
        if (!ApiManager.getInstance().getGenresList().isEmpty()) {
            mGenresAdapter = new GenresAdapter(ApiManager.getInstance().getGenresList());
            mRecyclerView.setAdapter(mGenresAdapter);
            return true;
        }
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        ContentManager.getInstance().stopProcess();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadGenres();
    }

}
