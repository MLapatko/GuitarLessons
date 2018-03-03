package com.example.user.guitarlessons.songsListScreen;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.model.Song;
import com.example.user.guitarlessons.songContentScreen.SongContentActivity;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by user on 27.02.2018.
 */

class GenresAdapter extends ExpandableRecyclerViewAdapter<GenresAdapter.GenreViewHolder, GenresAdapter.SongViewHolder> {

    public GenresAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    public void setList(List<? extends ExpandableGroup> groups) {
        if (groups != null) {
            expandableList.groups = groups;
            notifyDataSetChanged();
        }
    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genres_list_item, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public SongViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songs_list_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SongViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Song song = (Song) group.getItems().get(childIndex);
        holder.onBind(song);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongContentActivity.start(view.getContext(),song.getObjectId());
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
    }

    public class GenreViewHolder extends GroupViewHolder {
        TextView genreTitle;
        ImageView imageView;

        public GenreViewHolder(View itemView) {
            super(itemView);
            genreTitle = itemView.findViewById(R.id.genre_name);
            imageView = itemView.findViewById(R.id.action_image);
        }

        public void setGenreTitle(ExpandableGroup group) {
            genreTitle.setText(group.getTitle());
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

    public class SongViewHolder extends ChildViewHolder {

        TextView songTitle;
        TextView author;
        ImageView contentType;
        TextView tabsTextView;
        TextView chordsTextView;

        public SongViewHolder(View itemView) {
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
}
