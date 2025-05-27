package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.RestoreFavoriteExcursionUseCase
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.RestoreFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestoreFavoriteExcursionUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val excursionsRepository: ExcursionsRepository
): RestoreFavoriteExcursionUseCase {
    override suspend operator fun invoke(excursion: Excursion): Flow<UIResources<RestoreFavoriteExcursionResponse>> {
        val profile = profileRepository.profileFlow.value
        return excursionsRepository.restoreFavoriteExcursion(excursion,profile)
    }
}