package com.example.newsgateway;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ArticleFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<ArticleFragment> articleFragmentsList;
    private long baseId= 0;

    public ArticleFragmentAdapter(FragmentManager fm, ArrayList<ArticleFragment> fragmentArrayList) {
        super(fm);
        this.articleFragmentsList = fragmentArrayList;
    }

    @Override
    public Fragment getItem(int i) {
        return articleFragmentsList.get(i);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return articleFragmentsList.indexOf(object);
    }

    // return the total number of articles
    @Override
    public int getCount() {
        return articleFragmentsList.size();
    }

    @Override
    public long getItemId(int position) {
        return baseId + position;
    }

    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }
}
