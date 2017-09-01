package com.example.android.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsapp.NewsListFragment.LOG_TAG;

/**
 * Created by anu on 21/8/17.
 */

public class QueryUtils {

    public static final int SUCCESSFUL_RESPONSE_CODE = 200;
    private static Context mContext;

    //An empty private constructor so that the class is not going to be initialised.
    private QueryUtils() {
    }

    /**
     * Query the News dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsListData(String requestUrl, Context context) {
        mContext = context;
        URL url = null;
        url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link book}s
        List<News> newArticles = extractFeatureFromJson(jsonResponse);
        // Return the list
        return newArticles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(10000 /* milliseconds */);
            //urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == SUCCESSFUL_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String newsJson) {
        Bitmap imageIcon = null;
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<News> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJson);
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = responseObject.getJSONArray("results");
            String newsAuthor = "";
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject newsObject = resultsArray.getJSONObject(i);
                String newsTitle = newsObject.getString("webTitle");
                String newsSection = newsObject.getString("sectionName");
                String newsDate = newsObject.getString("webPublicationDate");
                String newsUrl = newsObject.getString("webUrl");
                if (newsObject.has("tags")) {
                    JSONArray tagsArray = newsObject.getJSONArray("tags");
                    if (tagsArray.length() > 0) {
                        for (int j = 0; j < 1; j++) {
                            JSONObject newsTag = tagsArray.getJSONObject(j);
                            if (newsTag.has("webUrl")) {
                                newsAuthor = newsTag.getString("webTitle");
                            }
                        }
                    }
                }
                News news = new News(newsTitle, newsAuthor, newsSection, newsDate, newsUrl);
                newsList.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Return the list of news items
        return newsList;
    }
}
