package com.example.user.guitarlessons;

import android.os.Bundle;

/**
 * Created by user on 21.02.2018.
 */

public class CoursesListFragment extends BaseFragment {
    public static CoursesListFragment newInstance() {

        Bundle args = new Bundle();

        CoursesListFragment fragment = new CoursesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.courses_list_fragment;
    }
}
