package com.gcml.lib_common.repository;


import com.gcml.lib_common.repository.di.RepositoryComponent;
import com.gcml.lib_common.repository.di.RepositoryModule;

public interface IRepositoryApp {

    RepositoryComponent repositoryComponent();

    RepositoryModule repositoryModule();
}
