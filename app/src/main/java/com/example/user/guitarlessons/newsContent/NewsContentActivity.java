package com.example.user.guitarlessons.newsContent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.guitarlessons.BaseActivity;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.NewsManager;
import com.example.user.guitarlessons.model.NewsItem;

import java.text.ParseException;

/**
 * Created by user on 13.03.2018.
 */

public class NewsContentActivity extends BaseActivity {
    public static void start(Context context, int id) {
        Intent starter = new Intent(context, NewsContentActivity.class);
        starter.putExtra(ID_NEWS, id);
        context.startActivity(starter);
    }

    public static final String ID_NEWS = "id news";
    private NewsItem mNews;
    private ImageView mNewsImage;
    private WebView mWebView;
    private TextView mDateTextView;
    private TextView mTitleTextView;

    @Override
    protected int getToolBarId() {
        return R.id.news_toolbar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNews = NewsManager.getInstance().findNewsSet(getIntent().getIntExtra(ID_NEWS, 0));
        mNewsImage = findViewById(R.id.news_image);
        mWebView = findViewById(R.id.news_content);
        mDateTextView=findViewById(R.id.news_date);
        mTitleTextView=findViewById(R.id.news_title);

        setBackButtonStatus(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        fillData();
    }

    private void fillData() {
        Glide.with(this)
                .load(mNews.getImage())
                .error(R.drawable.ic_picture)
                .into(mNewsImage);

        mWebView.loadData(String.valueOf(NewsManager.newsContentHelper(mNews.getDescription(),
                mNews.getImage(),mNews.getTitle())), "text/html", "utf-8");
        try {
            mDateTextView.setText(NewsManager.formatDate(mNews.getPublishDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mTitleTextView.setText(mNews.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT,mNews.getTitle());
                i.putExtra(Intent.EXTRA_TEXT, mNews.getLink());
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
