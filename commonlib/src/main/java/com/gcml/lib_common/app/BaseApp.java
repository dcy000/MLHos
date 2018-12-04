package com.gcml.lib_common.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.gcml.lib_common.lifecycle.AppDelegate;
import com.gcml.lib_common.lifecycle.TopActivityHelper;
import com.gcml.lib_common.util.business.UiUtils;
import com.gcml.lib_common.util.common.T;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by lenovo on 2018/11/29.
 */

public class BaseApp extends Application {
    public static Application app;

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
        app = this;
        initToast();
        initBugly();
        initXFVoice();
        screenAdaptation();
        registerActivityLifecycleCallbacks(new TopActivityHelper());
    }

    private void initToast() {
        T.init(this);
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
//        CrashReport.initCrashReport(getApplicationContext(), "082d769382", true);
    }

    private void screenAdaptation() {
        UiUtils.init(this, 1920, 1200);
    }

}
