package com.example.GuideExpert.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.GuideExpert.data.local.models.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM imageEntity WHERE excursionId = :id")
    fun getByExcursionId(id: Int) : Flow<List<ImageEntity>>

    @Query("SELECT * FROM imageEntity WHERE id = :id")
    fun getById(id: Int) : Flow<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<ImageEntity>)

}