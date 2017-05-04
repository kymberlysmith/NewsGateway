package com.example.k.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by K on 4/29/2017.
 */



public class NewsService extends Service implements Serializable {

    private static final String TAG = "NewsService";
    private boolean running = true;
    AtomicBoolean set;
    //private ArrayList<Article> articleArrayList = new ArrayList<>();
    private List articleArrayList = Collections.synchronizedList(new ArrayList<>());


    private static final String BROADCAST_ARTICLES_LIST="BROADCAST_ARTICLES_LIST";
    private static final String ARTICLES_LIST="ARTICLES_LIST";

    private ServiceReceiver serviceReceiver;

    public NewsService(){}

    @Override
    public void onCreate() {
        super.onCreate();
        serviceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter(MainActivity.BROADCAST_ARTICLES_REQUEST);
        registerReceiver(serviceReceiver, filter);

        set=new AtomicBoolean();

        Log.d(TAG, " onCreate");
    }

    public void setArticles(List a){
        /*if (!(articleArrayList == null)) {
            articleArrayList.clear();

        }*/

        articleArrayList=Collections.synchronizedList(new ArrayList<>(a));
        set = new AtomicBoolean(true);
        Log.d(TAG, " setArticles: articleArrayList set, size: " + articleArrayList.size()+", set = " +set);

        sendMessage("plz");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        sendMessage("Service Started");
        Log.d(TAG, " onStartCommand");
        //new thread for long running tasks because of ANR

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 30; i++) {
                    if (!running) {
                        Log.d(TAG, "run: Thread loop stopped early");
                        break;
                    }

                    Log.d(TAG, "run: " + i);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "thread val set = " + set);
                    if(set.toString().equals("true"))
                    {
                        sendBroadcast(intent);
                        Log.d(TAG, "onStartCommand, length of articleArrayList = " + articleArrayList.size());
                        sendMessage("Service Broadcast Message " + Integer.toString(i + 1));
                    }
                }
                sendMessage("Service Done Sending Broadcasts");
                Log.d(TAG, "run: Ending loop");
            }
        }).start();
        return Service.START_NOT_STICKY;

    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        unregisterReceiver(serviceReceiver);
        running=false;
        super.onDestroy();
    }

    private void sendMessage(String msg){

        Log.d(TAG, "sendMessage, length of articleArrayList " + articleArrayList.size());

        if(articleArrayList.size()==0)
        {
            Log.d(TAG, "sendMessage: articleArrayList is empty");
        }
        else
            {
                ArrayList<Article> arrayList = new ArrayList<Article>(articleArrayList);

                Log.d(TAG, "sendMessage: arrayList size "+arrayList.size());
                Intent intent = new Intent();
                intent.setAction(MainActivity.BROADCAST_ARTICLES_LIST);
                intent.putExtra(MainActivity.ARTICLES_LIST, arrayList);
                sendBroadcast(intent);
            }
    }

    public class ServiceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String source;
            Log.d(TAG, "ServiceReceiver: before switch statement");
            switch(intent.getAction()){

                case MainActivity.BROADCAST_ARTICLES_REQUEST:

                    if (intent.hasExtra(MainActivity.SOURCE_DATA)) {
                        source = intent.getStringExtra(MainActivity.SOURCE_DATA);
                        new NewsArticleDownloader(source).execute();
                    }
                    Log.d(TAG, "ServiceReceiver: Source broadcast received");
                    break;
                case MainActivity.BROADCAST_SOURCE_REQUEST:
                    String data="";
                    if (intent.hasExtra(MainActivity.SOURCE_DATA))
                        data=intent.getStringExtra(MainActivity.SOURCE_DATA);
                    //we're gonna do some stuff with this data
                    break;
            }

        }
    }
}

