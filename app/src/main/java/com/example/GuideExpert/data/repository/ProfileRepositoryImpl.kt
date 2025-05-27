package com.example.GuideExpert.data.repository

import com.example.GuideExpert.data.local.DBStorage
import com.example.GuideExpert.data.mappers.toAvatar
import com.example.GuideExpert.data.mappers.toProfile
import com.example.GuideExpert.data.mappers.toRemoveAvatarProfileResponse
import com.example.GuideExpert.data.mappers.toUpdateProfileResponse
import com.example.GuideExpert.data.remote.services.ProfileService
import com.example.GuideExpert.domain.models.Avatar
import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.Profile
import com.example.GuideExpert.domain.models.ProfileResources
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ProfileRepository
import com.example.GuideExpert.domain.repository.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Date
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dbStorage : DBStorage,
    private val profileService: ProfileService,
    private val sessionManager: SessionManager,
): ProfileRepository {
    private val _profileFlow = MutableStateFlow<Profile?>(null)
    override val profileFlow: StateFlow<Profile?> get() = _profileFlow

    private val _profileStateFlow = MutableStateFlow<ProfileResources>(ProfileResources.Idle)
    override val profileStateFlow: StateFlow<ProfileResources> get() = _profileStateFlow


    override suspend fun updateAvatarProfile(imagePart: MultipartBody.Part): Flow<UIResources<Avatar>> =
        flow {
            try {
                emit(UIResources.Loading)
                val result = profileService.updateAvatarProfile(
                    profileFlow.value?.id.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull()), imagePart
                )
                if (result.code() == 403) {
                    removeProfile()
                }
                if (result.isSuccessful) {
                    val newAvatar = result.body()?.toAvatar(profileFlow.value?.id!!) ?: Avatar()
                    if (newAvatar.id != null) {
                        profileFlow.value?.let {
                            updateProfile(it.copy(avatar = newAvatar))
                        }
                        emit(UIResources.Success(newAvatar))
                    } else {
                        emit(UIResources.Error("Avatar loaded error"))
                    }
                } else {
                    emit(UIResources.Error("Error loaded avatar"))
                }
            } catch (e: Exception) {
                emit(UIResources.Error(e.message.toString()))
            }

        }

    override suspend fun fetchProfile() {
        try {
            val id = runBlocking {
                sessionManager.getProfileId().first()
            }
            val profileId = if (id.isNotEmpty()) id.toInt() else 0

            if (profileId != 0) {
                _profileStateFlow.update { ProfileResources.Loading }
                val localProfile = dbStorage.getProfile(profileId).firstOrNull()
                if (localProfile !== null) {
                    _profileFlow.update { localProfile }
                }
                val result = profileService.getProfile(profileId)
                if (result.code() == 403) {
                    removeProfile()
                    _profileStateFlow.update { ProfileResources.Error("Error authorization") }
                }

                if (result.isSuccessful) {
                    val profile = result.body()?.toProfile()
                    _profileFlow.update { profile }
                    dbStorage.insertProfile(profile!!)
                    _profileStateFlow.update { ProfileResources.Success }
                }
            }
        } catch (e:Exception) {
             _profileStateFlow.update { ProfileResources.Error(e.message.toString()) }
         }
    }

    override suspend fun updateProfile(newProfile: Profile) {
        _profileFlow.update { newProfile }
        dbStorage.insertProfile(newProfile)
    }

    override suspend fun removeProfile() {
        sessionManager.setAuthToken("")
        sessionManager.setProfileId("")
        sessionManager.setProfileTime(0)
        _profileFlow.update { null }
        _profileStateFlow.update { ProfileResources.Idle }
        dbStorage.clearAll()
    }

    override suspend fun updateProfile(firstName: String, lastName: String, sex: String?, email:String, birthday: Date): Flow<UIResources<MessageResponse>> = flow {
        try {
            emit(UIResources.Loading)
            profileFlow.value?.let {
                val result = profileService.updateProfile(it.id,firstName,lastName,sex,email,birthday)
                if (result.code() == 403) {
                    removeProfile()
                }
                if (result.isSuccessful) {
                   val response = result.body()?.toUpdateProfileResponse() ?: MessageResponse()
                    if (response.success) {
                        profileFlow.value?.let {
                            updateProfile(it.copy(firstName = firstName, lastName =  lastName, sex = sex, email = email, birthday = birthday))
                        }
                        emit(UIResources.Success(response))
                    } else {
                        emit(UIResources.Error("Error :: ${response.message}"))
                    }
                }
            }
            if (profileFlow.value == null) {
                emit(UIResources.Error("Error update profile"))
            }

        } catch (e: Exception) {
            emit(UIResources.Error(e.message.toString()))
        }
    }

    override suspend fun removeAvatarProfile(): Flow<UIResources<MessageResponse>> = flow {
        try {
            emit(UIResources.Loading)
            val result = profileService.removeAvatarProfile(profileFlow.value?.id!!)
             if (result.code() == 403) {
                removeProfile()
            }
            if (result.isSuccessful) {
                val response =
                    result.body()?.toRemoveAvatarProfileResponse() ?: MessageResponse()
                if (response.success) {
                    profileFlow.value?.let {
                        updateProfile(it.copy(avatar = null))
                    }
                    emit(UIResources.Success(response))
                } else {
                    emit(UIResources.Error("Error :: ${response.message}"))
                }
            } else {
                emit(UIResources.Error("Error removed avatar"))
            }
        } catch (e: Exception) {
            emit(UIResources.Error(e.message.toString()))
        }
    }

}