package com.example.GuideExpert.data.di

import android.content.Context
import androidx.room.Room
import com.example.GuideExpert.data.local.db.ExcursionsRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalSourceModuleProvider {

    @Provides
    fun provideExcursionsFavoriteDao(database: ExcursionsRoomDatabase) = database.excursionsFavoriteDao()

    @Provides
    fun provideFavoriteDao(database: ExcursionsRoomDatabase) = database.favoriteDao()

    @Provides
    fun provideProfileDao(database: ExcursionsRoomDatabase) = database.profileDao()

    @Provides
    fun provideExcursionDataDao(database: ExcursionsRoomDatabase) = database.excursionDataDao()

    @Provides
    fun provideImageDao(database: ExcursionsRoomDatabase) = database.imageDao()

    @Provides
    fun provideExcursionSearchDao(database: ExcursionsRoomDatabase) = database.excursionSearchDao()

    @Provides
    fun provideExcursionFilterDao(database: ExcursionsRoomDatabase) = database.excursionFilterDao()

    @Provides
    fun provideRemoteKeyDao(database: ExcursionsRoomDatabase) = database.remoteKeyDao()

    @Provides
    @Singleton
    fun providesLocalDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ExcursionsRoomDatabase::class.java,
        "excursions-database"
    ).build()
}