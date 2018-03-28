package com.example.user.guitarlessons.ui.songslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ViewSwitcher;

import com.example.user.guitarlessons.base.BaseFragment;
import com.example.user.guitarlessons.FilterLayout;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.ApiManager;
import com.example.user.guitarlessons.managers.ContentManager;
import com.example.user.guitarlessons.model.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21.02.2018.
 */

public class SongsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RadioGroup.OnCheckedChangeListener {
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
    private FilterLayout filterLayout;

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

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        filterLayout=view.findViewById(R.id.filterLayout);

        filterLayout.setListener(new FilterLayout.FilterListener() {
            List<Genre> genres=new ArrayList<>();
            @Override
            public void onAllClicked() {
                genres=ApiManager.getInstance().getGenresList();
                if (!genres.isEmpty()) {
                    mGenresAdapter = new GenresAdapter(genres);
                    mRecyclerView.setAdapter(mGenresAdapter);
                }else {
                    onRefresh();
                }
            }

            @Override
            public void onChordsClicked() {
                genres=ApiManager.getInstance().getChordsSongs();
                mGenresAdapter = new GenresAdapter(genres);
                mRecyclerView.setAdapter(mGenresAdapter);
            }

            @Override
            public void onTabsClicked() {
                genres=ApiManager.getInstance().getTabsSongs();
                mGenresAdapter = new GenresAdapter(genres);
                mRecyclerView.setAdapter(mGenresAdapter);
            }
        });
        filterLayout.refreshData();
    }

    private void loadGenres() {

        mViewSwitcher.setDisplayedChild(0);
        ContentManager.getInstance().loadGenres(new ContentManager.ContentListener<List<Genre>>() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (!genres.isEmpty()){
                ApiManager.getInstance().updateSongs(genres);
                filterLayout.refreshData();}
                else {
                    mViewSwitcher.setDisplayedChild(1);
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        List<Genre> genres=new ArrayList<>();
        switch (i)
        {
            case R.id.radio_all:
               genres=ApiManager.getInstance().getGenresList();
                break;
            case R.id.radio_chords:
                genres=ApiManager.getInstance().getChordsSongs();
                break;
            case R.id.radio_tabs:
                genres=ApiManager.getInstance().getTabsSongs();
                break;
        }
        if (!genres.isEmpty()) {
            mGenresAdapter = new GenresAdapter(genres);
            mRecyclerView.setAdapter(mGenresAdapter);
        }
    }

}
