package com.example.GuideExpert.domain

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.models.Avatar
import com.example.GuideExpert.domain.models.ExcursionData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface UpdateAvatarProfileUseCase {
    suspend operator fun invoke(imagePart: MultipartBody.Part) : Flow<UIResources<Avatar>>
}