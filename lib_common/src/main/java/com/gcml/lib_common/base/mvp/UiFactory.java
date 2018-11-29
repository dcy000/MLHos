package com.gcml.lib_common.base.mvp;

/**
 * Created by afirez on 2017/7/13.
 */

public interface UiFactory<V extends IView, P extends IPresenter> {
    P providePresenter(V view);

    V provideView();
}
