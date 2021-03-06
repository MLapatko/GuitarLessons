package com.example.user.guitarlessons.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 15.02.2018.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract int getFragmentLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayoutId(), container, false);
    }
}
