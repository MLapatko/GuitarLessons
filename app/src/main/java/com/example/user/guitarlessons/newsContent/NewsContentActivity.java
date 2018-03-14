package com.example.user.guitarlessons.newsContent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.guitarlessons.BaseActivity;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.NewsManager;
import com.example.user.guitarlessons.model.NewsItem;

/**
 * Created by user on 13.03.2018.
 */

public class NewsContentActivity extends BaseActivity {
    public static void start(Context context,int id) {
        Intent starter = new Intent(context, NewsContentActivity.class);
        starter.putExtra(ID_NEWS,id);
        context.startActivity(starter);
    }

    public static final String ID_NEWS="id news";
    private NewsItem mNews;
    private ImageView mNewsImage;
    private WebView mWebView;
    @Override
    protected int getToolBarId() {
        return R.id.tool_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNews=NewsManager.getInstance().findNewsSet(getIntent().getIntExtra(ID_NEWS,0));
        mNewsImage=findViewById(R.id.news_image);
        mWebView=findViewById(R.id.news_content);
        fillData();
    }

    private void fillData() {
        Glide.with(this)
                .load(mNews.getImage())
                .error(R.drawable.ic_picture)
                .into(mNewsImage);
    }
}
