package com.example.GuideExpert.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.GuideExpert.data.local.Converters
import com.example.GuideExpert.data.local.dao.ExcursionDataDao
import com.example.GuideExpert.data.local.dao.ExcursionFilterDao
import com.example.GuideExpert.data.local.dao.ExcursionSearchDao
import com.example.GuideExpert.data.local.dao.ExcursionsFavoriteDao
import com.example.GuideExpert.data.local.dao.FavoriteDao
import com.example.GuideExpert.data.local.dao.ImageDao
import com.example.GuideExpert.data.local.dao.ProfileDao
import com.example.GuideExpert.data.local.dao.RemoteKeyDao
import com.example.GuideExpert.data.local.models.AvatarEntity
import com.example.GuideExpert.data.local.models.ExcursionDataEntity
import com.example.GuideExpert.data.local.models.ExcursionFavoriteEntity
import com.example.GuideExpert.data.local.models.ExcursionFilterEntity
import com.example.GuideExpert.data.local.models.ExcursionSearchEntity
import com.example.GuideExpert.data.local.models.ExcursionsFavoriteEntity
import com.example.GuideExpert.data.local.models.ImageEntity
import com.example.GuideExpert.data.local.models.ImagePreviewFavoriteEntity
import com.example.GuideExpert.data.local.models.ImagePreviewFilterEntity
import com.example.GuideExpert.data.local.models.ImagePreviewSearchEntity
import com.example.GuideExpert.data.local.models.ProfileEntity
import com.example.GuideExpert.data.local.models.RemoteKeyEntity


@Database(entities = [ExcursionSearchEntity::class, RemoteKeyEntity::class, ExcursionFilterEntity::class,
                    ImageEntity::class, ExcursionDataEntity::class, ImagePreviewSearchEntity::class,
                    ImagePreviewFilterEntity::class, ProfileEntity::class, AvatarEntity::class,
                    ExcursionFavoriteEntity::class, ExcursionsFavoriteEntity::class,
                    ImagePreviewFavoriteEntity::class], version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class ExcursionsRoomDatabase: RoomDatabase() {
    abstract fun excursionSearchDao() : ExcursionSearchDao
    abstract fun excursionFilterDao() : ExcursionFilterDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun imageDao(): ImageDao
    abstract fun excursionDataDao(): ExcursionDataDao
    abstract fun profileDao(): ProfileDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun excursionsFavoriteDao(): ExcursionsFavoriteDao
}