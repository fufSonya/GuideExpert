package com.example.GuideExpert.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.GuideExpert.data.local.models.ExcursionFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM excursionFavoriteEntity")
    fun getAll() : Flow<List<ExcursionFavoriteEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExcursion(excursions: List<ExcursionFavoriteEntity>)

    @Query("DELETE FROM excursionFavoriteEntity")
    suspend fun deleteAll()

    @Transaction
    suspend fun insertAll(excursions: List<ExcursionFavoriteEntity>) {
        deleteAll()
        insertAllExcursion(excursions)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(excursion: ExcursionFavoriteEntity)

    @Delete
    suspend fun delete(excursion: ExcursionFavoriteEntity)

}