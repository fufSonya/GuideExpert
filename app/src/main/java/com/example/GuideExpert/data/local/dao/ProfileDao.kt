package com.example.GuideExpert.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.GuideExpert.data.local.models.AvatarEntity
import com.example.GuideExpert.data.local.models.ProfileEntity
import com.example.GuideExpert.data.local.models.ProfileWithAvatar
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Transaction
    @Query("SELECT * FROM profileEntity WHERE id = :id")
    fun getById(id: Int) : Flow<ProfileWithAvatar?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: ProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvatar(avatar: AvatarEntity)

    @Query("DELETE FROM avatarEntity  WHERE profileId = :profileId")
    suspend fun deleteAvatarById(profileId:Int)

    @Transaction
    suspend fun insertAll(profileWithAvatar: ProfileWithAvatar) {
        insert(profileWithAvatar.profile)
        deleteAvatarById(profileWithAvatar.profile.id)
        profileWithAvatar.avatar?.let { insertAvatar(it) }
    }
}