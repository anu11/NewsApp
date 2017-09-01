package com.example.android.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by anu on 21/8/17.
 */

public class NewsListLoader extends AsyncTaskLoader<List<News>> {
    private static final String LOG_TAG = NewsListLoader.class.getSimpleName();
    private String mUrl;

    public NewsListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform network request, parse the response, and extract a list of news items
        List<News> newsItems = QueryUtils.fetchNewsListData(mUrl, getContext());
        return newsItems;
    }
}
