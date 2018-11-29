package com.gcml.lib_common.repository.di;

import android.app.Application;

import com.gcml.lib_common.repository.IRepositoryHelper;
import com.gcml.lib_common.repository.RepositoryHelper;
import com.gcml.lib_common.repository.cache.Cache;
import com.gcml.lib_common.repository.cache.CacheType;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

@Module
public class RepositoryModule {
    private Application mApplication;

    public RepositoryModule(Application application) {
        mApplication = application;
    }

    @Singleton
    @Provides
    IRepositoryHelper provideRepositoryHelper(
            Lazy<Retrofit> retrofit,
            Lazy<RxCache> rxCache,
            Cache.Factory cacheFactory,
            DbModule.RoomConfiguration roomConfiguration) {
        return new RepositoryHelper(mApplication, retrofit, rxCache, cacheFactory, roomConfiguration);
    }

    @Singleton
    @Provides
    Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS_CACHE_TYPE);
    }
}
