package com.example.user.guitarlessons;

import android.os.Bundle;

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

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.favorite_fragment;
    }
}
