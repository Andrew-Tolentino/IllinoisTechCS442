package com.example.newsgateway;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class ArticleService extends Service {

    private boolean isRunning = true;
    private ArrayList<Article> arrayList = new ArrayList<>();

    // TODO: Make an Article class

    public ArticleService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRunning) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Service",  "In Service Thread");
                    // Receiver will look for article requests
                    ArticleReceiver articleReceiver = new ArticleReceiver(arrayList);
                    IntentFilter filter = new IntentFilter(MainActivity.BROADCAST_ARTICLE_REQ);
                    registerReceiver(articleReceiver, filter);



                    while(isRunning) {

                        while(arrayList.isEmpty()) {
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }

                        // arrayList is not empty meaning that it is ready to broadcast

                        Intent intent = new Intent();
                        intent.setAction(MainActivity.BROADCAST_RECEIVED_ARTICLES);
                        intent.putExtra("articleList", arrayList);
                        sendBroadcast(intent);

                        // Empty arrayList
                        arrayList.clear();



                    }

                    // Unregister the receiver when isRunning is false
                    //unregisterReceiver(articleReceiver);


                }
            }).start();
        }



        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }
}
