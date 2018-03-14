package com.example.user.guitarlessons.model;

import me.toptas.rssconverter.RssItem;

/**
 * Created by user on 14.03.2018.
 */

public class NewsItem extends RssItem {
    private String title;
    private String link;
    private String image;
    private String publishDate;
    private String description;

    @Override
    public int hashCode() {
        int prime=31;
        return prime+getTitle().hashCode()+getPublishDate().hashCode();
    }

    public NewsItem(RssItem item) {
        if (item!=null){
            this.description =item.getDescription();
            this.image =item.getImage();
            this.link =item.getLink();
            this.publishDate =item.getPublishDate();
            this.title =item.getTitle();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", image='" + image + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
