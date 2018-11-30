package com.gcml.common;

import android.content.Context;

import com.gcml.common.repository.http.header.CommonHeaderInterceptor;
import com.gcml.lib_common.repository.di.RepositoryConfigModule;

public class CommonRepositoryConfig implements RepositoryConfigModule.Configuration {
    @Override
    public void configRepository(Context context, RepositoryConfigModule.Builder builder) {
        builder.addInterceptor(new CommonHeaderInterceptor());
    }
}
