package com.example.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class MainReciever extends BroadcastReceiver {
    private MainActivity mainActivity;

    public MainReciever(MainActivity ma) {
        this.mainActivity = ma;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MainReceiver", "onReceive: ");

        if (intent == null || intent.getAction() == null)  {
            return;
        }

        // If we receive a broadcast for the articles returning
        if (intent.getAction().equals(MainActivity.BROADCAST_RECEIVED_ARTICLES)) {
            // Do something with articles
            ArrayList<Article> arrayList = (ArrayList) intent.getSerializableExtra("articleList");

            Log.d("MainReceiver", arrayList.get(4).getAuthor());
            mainActivity.addAllFragments(arrayList);

        }
    }
}
