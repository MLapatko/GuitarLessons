package com.example.user.guitarlessons.managers;

import java.util.List;

import me.toptas.rssconverter.RssConverterFactory;
import me.toptas.rssconverter.RssFeed;
import me.toptas.rssconverter.RssItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    public void getNews(String url, final NewsListener listener){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://rock-school.by")
                .addConverterFactory(RssConverterFactory.create())
                .build();
        NewsService service = retrofit.create(NewsService.class);
        service.getRss(url)
                .enqueue(new Callback<RssFeed>() {
                    @Override
                    public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                        listener.onSuccess(response.body().getItems());
                    }

                    @Override
                    public void onFailure(Call<RssFeed> call, Throwable t) {
                        listener.onError(t);
                    }
                });
    }

    public interface NewsService{
        @GET
        Call<RssFeed> getRss(@Url String url);
    }
    public interface NewsListener {
        public void onSuccess(List<RssItem> item);
        public void onError(Throwable e);
    }
}
