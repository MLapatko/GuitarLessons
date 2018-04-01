package com.example.user.guitarlessons.ui.favoritelist;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.guitarlessons.base.BaseRecyclerViewAdapter;
import com.example.user.guitarlessons.ModelType;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.ui.lessoncontent.LessonContentActivity;
import com.example.user.guitarlessons.model.Lesson;
import com.example.user.guitarlessons.model.Song;
import com.example.user.guitarlessons.ui.songcontent.SongContentActivity;

/**
 * Created by user on 01.03.2018.
 */

public class FavoritesAdapter extends BaseRecyclerViewAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ModelType.LESSON_TYPE:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.lesson_list_item, parent, false);
                return new LessonsViewHolder(view);
            case ModelType.SONG_TYPE:
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.songs_list_item, parent, false);
                return new SongsViewHolder(view1);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        switch (getItemViewType(position)) {
            case ModelType.LESSON_TYPE:
                ((LessonsViewHolder) holder).onBind((Lesson) mList.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LessonContentActivity.start(view.getContext(), ((Lesson) mList.get(position)).getObjectId(), "");
                    }
                });
                break;
            case ModelType.SONG_TYPE:
                ((SongsViewHolder) holder).onBind((Song) mList.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SongContentActivity.start(view.getContext(), ((Song) mList.get(position)).getObjectId(), "");
                    }
                });
                break;
        }
    }

    public class SongsViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        TextView author;
        ImageView contentType;
        TextView tabsTextView;
        TextView chordsTextView;

        public SongsViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            contentType = itemView.findViewById(R.id.content_type);
            author = itemView.findViewById(R.id.author_name);
            chordsTextView = itemView.findViewById(R.id.chords);
            tabsTextView = itemView.findViewById(R.id.tabs);
        }

        public void onBind(Song song) {
            songTitle.setText(song.getTitle());
            author.setText(song.getAuthor());
            chordsTextView.setVisibility(View.GONE);
            tabsTextView.setVisibility(View.GONE);
            if (TextUtils.isEmpty(song.getVideoUrl())) {
                contentType.setImageResource(R.drawable.ic_music_player);
            } else {
                contentType.setImageResource(R.drawable.ic_students_cap);
            }
            if (song.getChords()) {
                chordsTextView.setVisibility(View.VISIBLE);
            }
            if (song.getTabs()) {
                tabsTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public class LessonsViewHolder extends RecyclerView.ViewHolder {
        TextView lessonTitle;
        ImageView contentType;

        public LessonsViewHolder(View itemView) {
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
