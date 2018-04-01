package com.example.user.guitarlessons.ui.courseslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewSwitcher;

import com.example.user.guitarlessons.base.BaseFragment;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.ApiManager;
import com.example.user.guitarlessons.managers.ContentManager;
import com.example.user.guitarlessons.model.Course;

import java.util.List;

/**
 * Created by user on 21.02.2018.
 */

public class CoursesListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static CoursesListFragment newInstance() {

        Bundle args = new Bundle();

        CoursesListFragment fragment = new CoursesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private CoursesAdapter mCoursesAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewSwitcher mViewSwitcher;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.courses_list_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mViewSwitcher = view.findViewById(R.id.viewSwitcher);
        mViewSwitcher.setDisplayedChild(0);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        if (!checkData()) {
            onRefresh();
        }
    }

    private void loadCourses() {

        mViewSwitcher.setDisplayedChild(0);
        ContentManager.getInstance().loadCourses(new ContentManager.ContentListener<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                if (mCoursesAdapter != null) {
                    mCoursesAdapter.setList(courses);
                } else {
                    mCoursesAdapter = new CoursesAdapter(courses);
                    mRecyclerView.setAdapter(mCoursesAdapter);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
                mViewSwitcher.setDisplayedChild(1);
            }
        });
    }

    public boolean checkData() {
        if (!ApiManager.getInstance().getCoursesList().isEmpty()) {
            mCoursesAdapter = new CoursesAdapter(ApiManager.getInstance().getCoursesList());
            mRecyclerView.setAdapter(mCoursesAdapter);
            return true;
        }
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSwipeRefreshLayout.isRefreshing()) {
            stopLoading();
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadCourses();
    }

    private void stopLoading() {
        ContentManager.getInstance().stopProcess();
        mSwipeRefreshLayout.setRefreshing(false);
        mViewSwitcher.setDisplayedChild(1);
    }
}
