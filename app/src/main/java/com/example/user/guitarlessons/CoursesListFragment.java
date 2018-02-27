package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.user.guitarlessons.managers.ContentManager;
import com.example.user.guitarlessons.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21.02.2018.
 */

public class CoursesListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "mylog";

    public static CoursesListFragment newInstance() {

        Bundle args = new Bundle();

        CoursesListFragment fragment = new CoursesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private List<Course> mCourses = new ArrayList<>();
    private CoursesAdapter mCoursesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        loadCourses();
    }

    private void loadCourses() {
        ContentManager.getInstance().loadCourses(new ContentManager.ContentListener<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                mCourses = courses;
                mCoursesAdapter = new CoursesAdapter(mCourses);
                mRecyclerView.setAdapter(mCoursesAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.courses_list_fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        ContentManager.getInstance().stopProcess();
    }

   @Override
    public void onRefresh() {
        loadCourses();
    }
}
