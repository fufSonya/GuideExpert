package com.example.GuideExpert.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.GuideExpert.data.local.models.ExcursionSearchEntity
import com.example.GuideExpert.data.local.models.ExcursionSearchWithData
import com.example.GuideExpert.data.local.models.ImagePreviewSearchEntity
import com.example.GuideExpert.data.mappers.toExcursionSearchEntity

@Dao
interface ExcursionSearchDao {

    @Transaction
    @Query("SELECT * FROM excursionSearchEntity ORDER BY idSort")
    fun pagingSource(): PagingSource<Int, ExcursionSearchWithData>

    @Query("DELETE FROM excursionSearchEntity")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExcursion(excursion: List<ExcursionSearchEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImagePreviewSearchEntity>)

    @Transaction
    suspend fun insertAll(excursions: List<ExcursionSearchWithData>) {
        insertExcursion(excursions.map{ it.toExcursionSearchEntity() })
        excursions.forEach { insertImages(it.images) }
    }

}