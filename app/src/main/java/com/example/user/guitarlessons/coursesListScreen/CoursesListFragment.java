package com.example.user.guitarlessons.coursesListScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewSwitcher;

import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.ApiManager;
import com.example.user.guitarlessons.managers.ContentManager;
import com.example.user.guitarlessons.model.Course;

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
    private CoursesAdapter mCoursesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
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

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setOnRefreshListener(this);
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
        ContentManager.getInstance().stopProcess();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadCourses();
    }
}
