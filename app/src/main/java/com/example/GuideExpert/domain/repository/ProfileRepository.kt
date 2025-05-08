package com.example.GuideExpert.domain.repository

import com.example.GuideExpert.data.repository.ProfileResources
import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.models.Avatar
import com.example.GuideExpert.domain.models.DeleteFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.ExcursionFavorite
import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.RestoreFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.SetFavoriteExcursionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MultipartBody
import java.util.Date

interface ProfileRepository {
    val profileFlow: StateFlow<Profile?>
    val profileStateFlow: StateFlow<ProfileResources>
    val profileFavoriteExcursionIdFlow: StateFlow<List<ExcursionFavorite>>
    suspend fun fetchProfile()
    suspend fun updateProfile(newProfile: Profile)
    suspend fun removeProfile()
    suspend fun updateAvatarProfile(imagePart: MultipartBody.Part): Flow<UIResources<Avatar>>
    suspend fun removeAvatarProfile():Flow<UIResources<MessageResponse>>
    suspend fun updateProfile(firstName: String, lastName: String, sex: String?, email:String, birthday: Date):Flow<UIResources<MessageResponse>>
    suspend fun getExcursionsFavorite()
    suspend fun updateExcursionsFavorite(excursions: List<ExcursionFavorite>)
    suspend fun setFavoriteExcursion(excursion: Excursion):Flow<UIResources<SetFavoriteExcursionResponse>>
    suspend fun removeFavoriteExcursion(excursion: Excursion):Flow<UIResources<DeleteFavoriteExcursionResponse>>
    suspend fun fetchExcursionsFavorite():Flow<UIResources<List<Excursion>?>>
    suspend fun restoreFavoriteExcursion(excursion: Excursion):Flow<UIResources<RestoreFavoriteExcursionResponse>>
}