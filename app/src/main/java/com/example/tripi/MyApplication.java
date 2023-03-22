package com.example.tripi;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    private static Context context;
    public static ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
//        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(gfgPolicy);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}