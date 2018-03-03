package com.example.user.guitarlessons.metronomeScreen;

import android.os.Bundle;

import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.R;

/**
 * Created by user on 22.02.2018.
 */

public class MetronomeFragment extends BaseFragment {
    public static MetronomeFragment newInstance() {

        Bundle args = new Bundle();

        MetronomeFragment fragment = new MetronomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.metronome_fragment;
    }
}
