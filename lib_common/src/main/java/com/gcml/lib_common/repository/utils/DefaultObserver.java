package com.gcml.lib_common.repository.utils;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * Created by afirez on 18-2-1.
 */

public class DefaultObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable throwable) {
        Timber.e(throwable);
    }

    @Override
    public void onComplete() {

    }
}
