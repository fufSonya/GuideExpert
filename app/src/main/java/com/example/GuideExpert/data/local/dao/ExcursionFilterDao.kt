package com.example.GuideExpert.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.GuideExpert.data.local.models.ExcursionFilterEntity
import com.example.GuideExpert.data.local.models.ExcursionFilterWithData
import com.example.GuideExpert.data.local.models.ImagePreviewFilterEntity
import com.example.GuideExpert.data.mappers.toExcursionFilterEntity

@Dao
interface ExcursionFilterDao {

    @Transaction
    @Query("SELECT * FROM excursionFilterEntity ORDER BY idSort")
    fun pagingSource(): PagingSource<Int, ExcursionFilterWithData>

    @Query("DELETE FROM excursionFilterEntity")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExcursion(excursion: List<ExcursionFilterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImagePreviewFilterEntity>)

    @Transaction
    suspend fun insertAll(excursions: List<ExcursionFilterWithData>) {
        insertExcursion(excursions.map{ it.toExcursionFilterEntity() })
        excursions.forEach { insertImages(it.images) }
    }
}