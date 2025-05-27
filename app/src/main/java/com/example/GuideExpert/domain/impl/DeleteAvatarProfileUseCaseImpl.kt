package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.DeleteAvatarProfileUseCase
import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAvatarProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): DeleteAvatarProfileUseCase {
    override suspend operator fun invoke(): Flow<UIResources<MessageResponse>> {
        return profileRepository.removeAvatarProfile()
    }
}