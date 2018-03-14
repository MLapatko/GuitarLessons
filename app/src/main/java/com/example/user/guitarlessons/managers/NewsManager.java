package com.example.user.guitarlessons.managers;

import com.example.user.guitarlessons.model.NewsItem;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import me.toptas.rssconverter.RssConverterFactory;
import me.toptas.rssconverter.RssFeed;
import me.toptas.rssconverter.RssItem;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by user on 07.03.2018.
 */

public class NewsManager {
    private static NewsManager instance;

    public static NewsManager getInstance() {
        if (instance == null) {
            return instance = new NewsManager();
        }

        return instance;
    }

    private Set<NewsItem> newsSet = new HashSet<>();

    public void setNewsSet(Set<NewsItem> news) {
        if (news != null) {
            this.newsSet.clear();
            this.newsSet.addAll(news);
        }
    }
    public NewsItem findNewsSet(int id){
        Iterator<NewsItem> iterator=this.newsSet.iterator();
        while (iterator.hasNext()){
            NewsItem item=iterator.next();
            if (item.hashCode()==id){
                return item;
            }
        }
        return null;
    }

    public Set<NewsItem> getNewsSet() {
        return newsSet;
    }

    public void getNews(String url, final NewsListener listener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://celebrity.moscow")
                .addConverterFactory(RssConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        NewsService service = retrofit.create(NewsService.class);
        service.getRss(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RssFeed>() {
                    @Override
                    public void onSuccess(RssFeed rssFeed) {
                        List<NewsItem> resultList = new ArrayList<>();
                        Set<NewsItem> news = new HashSet<>();
                        for (RssItem item : rssFeed.getItems()) {
                            news.add(new NewsItem(item));
                            resultList.add(new NewsItem(item));
                        }
                        setNewsSet(news);
                        if (listener != null) {
                            listener.onSuccess(resultList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onError(e);
                        }
                    }
                });
    }

    public interface NewsService {
        @GET
        Single<RssFeed> getRss(@Url String url);
    }

    public interface NewsListener {
        void onSuccess(List<NewsItem> item);

        void onError(Throwable e);
    }
}
