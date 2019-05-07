package com.example.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

// This receiver will be used in Article Service to look for Main Activity broadcast messages
public class ArticleReceiver extends BroadcastReceiver {

    private ArrayList<Article> arrayList = new ArrayList<>();

    public ArticleReceiver(ArrayList<Article> arrayList) {
        // Contains a reference to the arrayList in the ArticleService class
        this.arrayList = arrayList;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null)  {
            return;
        }

        // If we receive a broadcast asking for articles
        if (intent.getAction().equals(MainActivity.BROADCAST_ARTICLE_REQ)) {
            // We want to send a Broadcast message to Service to get Articles
            Log.d("ArticleReceiver", "From Article Receiver: " + intent.getStringExtra("id"));
            AsyncGetArticles asyncGetArticles = new AsyncGetArticles(intent.getStringExtra("id"), this.arrayList);
            asyncGetArticles.execute();
        }
    }
}
