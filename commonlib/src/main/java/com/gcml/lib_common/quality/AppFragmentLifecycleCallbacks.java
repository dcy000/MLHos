package com.gcml.lib_common.quality;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class AppFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {
    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        LeakCanaryHelper.INSTANCE.watcher().watch(f);
    }
}
