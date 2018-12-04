package com.gcml.locationlib;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by lenovo on 2018/12/4.
 */

public class LocationApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
