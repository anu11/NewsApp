package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by anu on 22/8/17.
 */

public class NewsListAdapter extends ArrayAdapter<News> {
    private static Context mContext;
    private List<News> mNewsList;

    public NewsListAdapter(Context context, List<News> newsItem) {
        super(context, 0, newsItem);
        mContext = context;
        mNewsList = newsItem;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
            holder.newsTitle = (TextView) convertView.findViewById(R.id.text_news_title);
            holder.sectionName = (TextView) convertView.findViewById(R.id.text_news_section);
            holder.authorName = (TextView)convertView.findViewById(R.id.text_news_author);
            holder.newsDate = (TextView)convertView.findViewById(R.id.text_news_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            // Find news at the given position in the list
            News currentItem = getItem(position);

            /** Set data to respective views within ListView */

            if ((currentItem.getmNewTitle() != null) && (currentItem.getmNewTitle().length() > 0)) {
                holder.newsTitle.setText(currentItem.getmNewTitle());
            }
            if ((currentItem.getmNewsSection() != null) && (currentItem.getmNewsSection().length() > 0)) {
                holder.sectionName.setText(currentItem.getmNewsSection());
            }
            if ((currentItem.getmNewsAuthor() != null) && (currentItem.getmNewsAuthor().length() > 0)) {
                holder.authorName.setText(currentItem.getmNewsAuthor());
            }
            if ((currentItem.getmNewsPublishDate() != null) && (currentItem.getmNewsPublishDate().length() > 0)) {
                String date = currentItem.getmNewsPublishDate();
                String newsDateFormat = formatDate(date);
                holder.newsDate.setText(newsDateFormat);
            }
        return convertView;
    }

    static class ViewHolder {
        TextView newsTitle;
        TextView sectionName;
        TextView authorName;
        TextView newsDate;
    }

    /**
     * Formats the date from yyyy-MM-dd to dd MMM,yyyy
     */
    public String formatDate(String date) {
        String formattedDate = "";
        String newDate = date.substring(0, 10);
        
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMM, yyyy");
        try {
            formattedDate = newDateFormat.format(oldDateFormat.parse(newDate));
        }
        catch(ParseException pe) {
        }
        return formattedDate;
    }

}
