package com.gcml.lib_common.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.gcml.lib_common.lifecycle.AppDelegate;
import com.gcml.lib_common.lifecycle.TopActivityHelper;
import com.gcml.lib_common.util.business.UiUtils;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by lenovo on 2018/11/29.
 */

public class BaseApp extends Application {
    public Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        AppDelegate.INSTANCE.attachBaseContext(this, base);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        AppDelegate.INSTANCE.onCreate(this);
        context = getApplicationContext();
        initBugly();
        initXFVoice();
        screenAdaptation();
        registerActivityLifecycleCallbacks(new TopActivityHelper());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppDelegate.INSTANCE.onTerminate(this);

    }


    private void initXFVoice() {
        SpeechUtility.createUtility(this, "appid=59196d96");
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "082d769382", true);
    }

    private void screenAdaptation() {
        UiUtils.init(this, 1920, 1200);
    }

}
