package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.GetExcursionDetailUseCase
import com.example.GuideExpert.domain.UpdateAvatarProfileUseCase
import com.example.GuideExpert.domain.models.Avatar
import com.example.GuideExpert.domain.models.ExcursionData
import com.example.GuideExpert.domain.repository.DataSourceRepository
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject


class UpdateAvatarProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): UpdateAvatarProfileUseCase {
    override suspend operator fun invoke(imagePart: MultipartBody.Part): Flow<UIResources<Avatar>> {
        return profileRepository.updateAvatarProfile(imagePart)
    }
}