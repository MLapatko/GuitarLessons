package com.example.user.guitarlessons.newsScreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.model.NewsItem;
import com.example.user.guitarlessons.newsContent.NewsContentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.toptas.rssconverter.RssItem;

/**
 * Created by user on 13.03.2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private List<NewsItem> mNews=new ArrayList<>();

    public void setList(List<NewsItem> list) {
        if (list != null) {
            this.mNews.clear();
            this.mNews.addAll(list);
        }
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.ViewHolder holder, int position) {
        final RssItem item=mNews.get(position);
        SimpleDateFormat dateFormat=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        try {
            Date date=dateFormat.parse(item.getPublishDate());
            SimpleDateFormat format=new SimpleDateFormat("dd.MM.yyyy");
            holder.newsDate.setText(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.newsTitle.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsContentActivity.start(view.getContext(),item.hashCode());
            }
        });
        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .error(R.drawable.ic_picture)
                .into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView newsTitle;
        ImageView newsImage;
        TextView newsDate;

        public ViewHolder(View itemView) {
            super(itemView);
            newsTitle =itemView.findViewById(R.id.news_title);
            newsImage=itemView.findViewById(R.id.news_image);
            newsDate=itemView.findViewById(R.id.news_date);
        }
    }
}
