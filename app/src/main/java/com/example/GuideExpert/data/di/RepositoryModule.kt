package com.example.GuideExpert.data.di

import com.example.GuideExpert.data.repository.DataPagingRepositoryImpl
import com.example.GuideExpert.data.repository.DataProviderRepositoryImpl
import com.example.GuideExpert.data.repository.DataSourceRepositoryImpl
import com.example.GuideExpert.data.repository.ProfileRepositoryImpl
import com.example.GuideExpert.domain.repository.DataPagingRepository
import com.example.GuideExpert.domain.repository.DataProviderRepository
import com.example.GuideExpert.domain.repository.DataSourceRepository
import com.example.GuideExpert.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModuleBinder {

    @Singleton
    @Binds
    abstract fun bindDataPagingRepository(
        dataPagingRepositoryImpl: DataPagingRepositoryImpl
    ) : DataPagingRepository

    @Singleton
    @Binds
    abstract fun bindDataSourceRepository(
        dataSourceRepositoryImpl: DataSourceRepositoryImpl
    ) : DataSourceRepository

    @Singleton
    @Binds
    abstract fun bindDataProviderRepository(
        dataProviderRepositoryImpl: DataProviderRepositoryImpl
    ) : DataProviderRepository

    @Singleton
    @Binds
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ) : ProfileRepository

}