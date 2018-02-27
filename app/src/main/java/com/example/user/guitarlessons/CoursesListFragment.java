package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.backendless.exceptions.BackendlessFault;
import com.example.user.guitarlessons.model.Course;
import com.example.user.guitarlessons.model.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21.02.2018.
 */

public class CoursesListFragment extends BaseFragment {

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getCourses();
    }

    private List<Course> getGenres() {
        List<Course> genres = new ArrayList<>();
        List<Lesson> lessons = getLessons();
        genres.add(new Course("title1", lessons));
        genres.add(new Course("title2", lessons));
        genres.add(new Course("title3", lessons));
        genres.add(new Course("title4", lessons));
        genres.add(new Course("title5", lessons));
        return genres;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.courses_list_fragment;
    }

    private void getCourses() {
        DbManager.getInstance().getCourses(new DbManager.DbListener<List<Course>>() {
            @Override
            public void onSuccess(List<Course> response) {
                for (Course course : response) {
                    course.setLessons(getLessons());
                }

                mCourses = response;
                mCoursesAdapter = new CoursesAdapter(mCourses);
                mRecyclerView.setAdapter(mCoursesAdapter);
            }

            @Override
            public void onError(BackendlessFault fault) {
                Log.d(TAG, fault.getMessage());
            }
        });
    }

    private List<Lesson> getLessons() {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson("1", "les1", "", "", "", 6));
        lessons.add(new Lesson("2", "les2", "", "", "", 6));
        lessons.add(new Lesson("3", "les3", "", "", "", 6));
        lessons.add(new Lesson("4", "les4", "", "", "", 6));
        lessons.add(new Lesson("5", "les5", "", "", "", 6));
        return lessons;
    }

    private void getLessons(String courseId) {
        DbManager.getInstance().getLessonsInCourse(courseId, new DbManager.DbListener<List<Lesson>>() {
            @Override
            public void onSuccess(List<Lesson> response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onError(BackendlessFault fault) {
                Log.d(TAG, fault.getMessage());
            }
        });
    }
}
