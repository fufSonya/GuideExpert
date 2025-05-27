package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.UpdateProfileUseCase
import com.example.GuideExpert.domain.models.MessageResponse
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class UpdateProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): UpdateProfileUseCase {
    override suspend operator fun invoke(firstName: String, lastName: String, sex: String?, email:String, birthday: Date): Flow<UIResources<MessageResponse>> {
        return profileRepository.updateProfile(firstName,lastName,sex,email,birthday)
    }
}