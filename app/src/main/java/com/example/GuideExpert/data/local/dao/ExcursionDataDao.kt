package com.example.GuideExpert.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.GuideExpert.data.local.models.ExcursionDataEntity
import com.example.GuideExpert.data.local.models.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExcursionDataDao {

    @Query("SELECT * FROM excursionDataEntity WHERE id = :id")
    fun getById(id: Int) : Flow<ExcursionDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExcursion(excursion: ExcursionDataEntity)

    @Query("SELECT * FROM imageEntity WHERE excursionId = :id")
    fun getImagesByExcursionId(id: Int) : Flow<List<ImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)

    @Query("DELETE FROM excursionDataEntity  WHERE id = :id")
    suspend fun deleteExcursionById(id:Int)

    @Transaction
    suspend fun insertExcursionAndImages(excursion:ExcursionDataEntity, images:List<ImageEntity>) {
        deleteExcursionById(excursion.id)
        insertExcursion(excursion)
        insertImages(images)
    }

}
