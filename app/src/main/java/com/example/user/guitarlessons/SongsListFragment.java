package com.example.user.guitarlessons;

import android.os.Bundle;

/**
 * Created by user on 21.02.2018.
 */

public class SongsListFragment extends BaseFragment {
    public static SongsListFragment newInstance() {

        Bundle args = new Bundle();

        SongsListFragment fragment = new SongsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.songs_list_fragment;
    }
}
