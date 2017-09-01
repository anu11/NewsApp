package com.example.android.newsapp;

/**
 * News POJO class depicting the contents of a single news item
 */

public class News {

    private String mNewTitle;
    private String mNewsAuthor;
    private String mNewsSection;
    private String mNewsPublishDate;
    private String mUrl;

    public News(String newTitle, String newsAuthor, String newsSection, String publishedDate, String newsUrl) {
        mNewTitle = newTitle;
        mNewsAuthor = newsAuthor;
        mNewsSection = newsSection;
        mNewsPublishDate = publishedDate;
        mUrl = newsUrl;
    }

    public String getmNewTitle() {

        return mNewTitle;
    }

    public String getmNewsAuthor() {

        return mNewsAuthor;
    }

    public String getmNewsSection() {

        return mNewsSection;
    }

    public String getmNewsPublishDate() {
        return mNewsPublishDate;
    }

    public String getmUrl() {

        return mUrl;
    }

}
