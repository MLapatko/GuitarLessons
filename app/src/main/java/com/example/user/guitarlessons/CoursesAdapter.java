package com.example.user.guitarlessons;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.guitarlessons.model.Lesson;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by user on 26.02.2018.
 */

public class CoursesAdapter extends ExpandableRecyclerViewAdapter<CoursesAdapter.CourseViewHolder, CoursesAdapter.LessonViewHolder> {


    public CoursesAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public CourseViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_list_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public LessonViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_list_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(LessonViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Lesson lesson = (Lesson) group.getItems().get(childIndex);
        holder.onBind(lesson);
    }

    @Override
    public void onBindGroupViewHolder(CourseViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setCourseTitle(group);
    }

    public class CourseViewHolder extends GroupViewHolder {
        TextView courseTitle;

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title);
        }

        public void setCourseTitle(ExpandableGroup group) {
            courseTitle.setText(group.getTitle());
        }
    }

    public class LessonViewHolder extends ChildViewHolder {
        TextView lessonTitle;

        public LessonViewHolder(View itemView) {
            super(itemView);
            lessonTitle = itemView.findViewById(R.id.lesson_title);
        }

        public void onBind(Lesson lesson) {
            lessonTitle.setText(lesson.getTitle());
        }
    }
}
