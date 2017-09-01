package com.example.android.newsapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by anu on 22/8/17.
 */

public class NewsPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_OF_ITEMS = 5;
    private Context mContext;

    public NewsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    /*Returns the Fragment requested by the user*/
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NewsListFragment.newInstance("home", 0);
            case 1:
                return NewsListFragment.newInstance("politics", 1);
            case 2:
                return NewsListFragment.newInstance("technology", 2);
            case 3:
                return NewsListFragment.newInstance("sport", 3);
            case 4:
                return NewsListFragment.newInstance("environment", 4);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_OF_ITEMS;
    }
    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Politics";
            case 2:
                return "Technology";
            case 3:
                return "Sport";
            case 4:
                return "Environment";
            default:
                return null;
        }
    }
}
