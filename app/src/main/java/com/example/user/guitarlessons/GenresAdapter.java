package com.example.user.guitarlessons;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.guitarlessons.model.Song;
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

        public SongViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
        }

        public void onBind(Song song) {
            songTitle.setText(song.getTitle());
        }
    }
}
