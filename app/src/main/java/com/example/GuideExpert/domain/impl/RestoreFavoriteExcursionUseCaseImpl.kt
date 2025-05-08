package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.RestoreFavoriteExcursionUseCase
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.RestoreFavoriteExcursionResponse
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestoreFavoriteExcursionUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): RestoreFavoriteExcursionUseCase {
    override suspend operator fun invoke(excursion: Excursion): Flow<UIResources<RestoreFavoriteExcursionResponse>> {
        return profileRepository.restoreFavoriteExcursion(excursion)
    }
}