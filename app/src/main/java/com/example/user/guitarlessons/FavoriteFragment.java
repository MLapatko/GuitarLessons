package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21.02.2018.
 */

public class FavoriteFragment extends BaseFragment {
    public static FavoriteFragment newInstance() {

        Bundle args = new Bundle();

        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private List<Object> favorites = new ArrayList<>();
    private ViewSwitcher mViewSwitcher;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewSwitcher = view.findViewById(R.id.viewSwitcher);

        favorites.addAll(ApiManager.getInstance().getFavoriteSongs());
        favorites.addAll(ApiManager.getInstance().getUsersLessons(ApiManager.COLUMN_FAVORITE));

        if (!favorites.isEmpty()) {
            mViewSwitcher.setDisplayedChild(0);
            FavoritesAdapter adapter = new FavoritesAdapter();
            adapter.setList(favorites);
            mRecyclerView.setAdapter(adapter);
        } else {
            mViewSwitcher.setDisplayedChild(1);
        }

    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.favorite_fragment;
    }

}
