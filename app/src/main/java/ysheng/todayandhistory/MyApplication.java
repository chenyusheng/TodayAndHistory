package ysheng.todayandhistory;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class MyApplication extends Application {
    private static MyApplication instance;
    public static MyApplication getInstance() {
        return instance;
    }
     @Override public void onCreate() {
         super.onCreate();
         instance = this;
         TypefaceProvider.registerDefaultIconSets();
     }
 }