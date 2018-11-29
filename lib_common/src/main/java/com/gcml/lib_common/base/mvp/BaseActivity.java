package com.gcml.lib_common.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gcml.lib_common.util.business.RxUtils;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * Created by afirez on 2017/7/11.
 */

public abstract class  BaseActivity<V extends IView, P extends IPresenter>
        extends AppCompatActivity
        implements IView, UiFactory<V, P> {
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = providePresenter(provideView());
        presenter.setLifecycleOwner(this);
        getLifecycle().addObserver(presenter);
    }

    public <T> AutoDisposeConverter<T> autoDisposeConverter() {
        return RxUtils.autoDisposeConverter(this);
    }
}