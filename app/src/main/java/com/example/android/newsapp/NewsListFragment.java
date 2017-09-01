package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anu on 22/8/17.
 */

public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    public static final String TAG = NewsListFragment.class.getSimpleName();
    public static final String LOG_TAG = NewsListFragment.class.getName();
    private static final String NEWS_BASE_URL = "http://content.guardianapis.com/search?q=";
    private static final String NEWS_SEARCH_URL = "https://content.guardianapis.com/search?section=";
    private static final int NEWS_LOADER_ID = 1;
    public List<News> mListNews;
    private String mUrl;
    private View mView;
    private NewsListAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ListView mNewsListView;

    // newInstance constructor for creating fragment with arguments
    public static NewsListFragment newInstance(String newsSection, int menuPosition) {
        NewsListFragment newsListFragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString("section", newsSection);
        args.putInt("position", menuPosition);
        newsListFragment.setArguments(args);
        return newsListFragment;
    }

    public String getSection() {
        return getArguments().getString("section", "");
    }
    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_news_list, container, false);

        // Find a reference to the {@link ListView} in the layout
        mNewsListView = (ListView) mView.findViewById(R.id.news_list);

        // Set empty view
        mEmptyStateTextView = (TextView) mView.findViewById(R.id.empty_view);
        mNewsListView.setEmptyView(mEmptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Hide loading indicator and show empty state view
            View progressIndicator = mView.findViewById(R.id.loading_indicator);
            progressIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(getString(R.string.error_no_connection));
        }

        // Create a new adapter that takes the list as input
        mListNews = new ArrayList<News>();
        mAdapter = new NewsListAdapter(getContext(), mListNews);
        mNewsListView.setAdapter(mAdapter);

        mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getmUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(webIntent);
            }
        });
        return mView;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        String section = getSection();
        String orderBy = "";
        if (section.equals("home")) {
            mUrl = NEWS_BASE_URL;
        } else {
            mUrl = NEWS_SEARCH_URL + section;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(searchQuery());
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("order-by", orderBy);
        return new NewsListLoader(getContext(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsdata) {
        View loadingIndicator = mView.findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        if (newsdata == null || newsdata.size() == 0) {
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(getString(R.string.no_news));
        } else {
            mEmptyStateTextView.setVisibility(View.GONE);
        }

        mListNews.clear();

        // If there is a valid list  then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsdata != null && !newsdata.isEmpty()) {
            mListNews.addAll(newsdata);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    /*Method to create the search query to hit the guardian API*/
    public String searchQuery() {
        StringBuilder stringBuilder = new StringBuilder(mUrl);
        stringBuilder.append("&show-tags=contributor");
        stringBuilder.append("&show-references=author");
        stringBuilder.append("&api-key=test");
        return stringBuilder.toString();
    }
}
