package com.example.user.guitarlessons;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.guitarlessons.model.Lesson;
import com.thoughtbot.expandablerecyclerview.ExpandCollapseController;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by user on 26.02.2018.
 */

public class CoursesAdapter extends ExpandableRecyclerViewAdapter<CoursesAdapter.CourseViewHolder,
        CoursesAdapter.LessonViewHolder> {


    public CoursesAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    public void setList(List<? extends ExpandableGroup> groups) {
        if (groups != null) {
            expandableList.groups = groups;
            int size = Math.abs(groups.size() - expandableList.expandedGroupIndexes.length);
            boolean index[] = new boolean[groups.size()];
            for (int i = groups.size() - 1; i >= size; i--) {
                index[i] = expandableList.expandedGroupIndexes[i - groups.size() + expandableList.expandedGroupIndexes.length];
            }

            expandableList.expandedGroupIndexes = index;
            ExpandCollapseController expandCollapseController=new ExpandCollapseController(expandableList, this);
            notifyDataSetChanged();

        }
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
        ImageView imageView;

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title);
            imageView = itemView.findViewById(R.id.action_image);
        }

        public void setCourseTitle(ExpandableGroup group) {
            courseTitle.setText(group.getTitle());
        }

        @Override
        public void expand() {
            imageView.animate().rotation(180).start();
        }

        @Override
        public void collapse() {
            imageView.animate().rotation(0).start();
        }
    }

    public class LessonViewHolder extends ChildViewHolder {
        TextView lessonTitle;
        ImageView contentType;

        public LessonViewHolder(View itemView) {
            super(itemView);
            lessonTitle = itemView.findViewById(R.id.lesson_title);
            contentType = itemView.findViewById(R.id.content_type);
        }

        public void onBind(Lesson lesson) {
            lessonTitle.setText(lesson.getTitle());
            if (TextUtils.isEmpty(lesson.getVideoUrl())) {
                contentType.setImageResource(R.drawable.ic_file);
            } else {
                contentType.setImageResource(R.drawable.ic_youtube_play_button);
            }
        }
    }
}
