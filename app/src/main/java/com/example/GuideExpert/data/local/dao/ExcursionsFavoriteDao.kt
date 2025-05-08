package com.example.GuideExpert.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.GuideExpert.data.local.models.ExcursionsFavoriteEntity
import com.example.GuideExpert.data.local.models.ExcursionsFavoriteWithData
import com.example.GuideExpert.data.local.models.ImagePreviewFavoriteEntity
import com.example.GuideExpert.data.mappers.toExcursionsFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExcursionsFavoriteDao {
    @Transaction
    @Query("SELECT * FROM excursionsFavoriteEntity ORDER BY timestamp DESC")
    fun getAll() : Flow<List<ExcursionsFavoriteWithData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(excursion: ExcursionsFavoriteEntity)

    @Transaction
    suspend fun insertExcursion(excursion: ExcursionsFavoriteWithData){
        insert(excursion.toExcursionsFavoriteEntity())
        insertImages(excursion.images)
    }

    @Delete
    suspend fun deleteExcursion(excursion: ExcursionsFavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImagePreviewFavoriteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExcursions(excursion: List<ExcursionsFavoriteEntity>)

    @Query("DELETE FROM excursionsFavoriteEntity")
    suspend fun clearAll()

    @Transaction
    suspend fun insertAll(excursions: List<ExcursionsFavoriteWithData>) {
        clearAll()
        insertExcursions(excursions.map{ it.toExcursionsFavoriteEntity() })
        excursions.forEach { insertImages(it.images) }
    }
}