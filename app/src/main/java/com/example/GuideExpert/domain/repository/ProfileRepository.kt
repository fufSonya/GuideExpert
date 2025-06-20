package com.example.GuideExpert.domain.repository

import com.example.GuideExpert.domain.models.Avatar
import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.ProfileResources
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MultipartBody
import java.util.Date

interface ProfileRepository {
    val profileFlow: StateFlow<Profile?>
    val profileStateFlow: StateFlow<ProfileResources>
    suspend fun fetchProfile()
    suspend fun updateProfile(newProfile: Profile)
    suspend fun removeProfile()
    suspend fun updateAvatarProfile(imagePart: MultipartBody.Part): Flow<UIResources<Avatar>>
    suspend fun removeAvatarProfile():Flow<UIResources<MessageResponse>>
    suspend fun updateProfile(firstName: String, lastName: String, sex: String?, email:String, birthday: Date):Flow<UIResources<MessageResponse>>
}