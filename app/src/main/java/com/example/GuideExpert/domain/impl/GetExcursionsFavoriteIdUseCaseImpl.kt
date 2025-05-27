package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionsFavoriteIdUseCase
import com.example.GuideExpert.domain.models.ErrorExcursionsRepository
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import com.example.GuideExpert.domain.repository.ProfileRepository
import javax.inject.Inject

class GetExcursionsFavoriteIdUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val excursionsRepository: ExcursionsRepository
): GetExcursionsFavoriteIdUseCase {
    override suspend operator fun invoke() {
        val profile = profileRepository.profileFlow.value
        profile?.let {
            val response  = excursionsRepository.getExcursionsFavorite(profile)
            when(response) {
                ErrorExcursionsRepository.Authorization -> {
                    profileRepository.removeProfile()
                    excursionsRepository.removeFavoriteExcursionIds()
                }
                else -> {}
            }
        }
    }
}