package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.SetFavoriteExcursionUseCase
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.SetFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetFavoriteExcursionUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val excursionsRepository: ExcursionsRepository
): SetFavoriteExcursionUseCase {
    override suspend operator fun invoke(excursion: Excursion): Flow<UIResources<SetFavoriteExcursionResponse>> {
        val profile = profileRepository.profileFlow.value
        return excursionsRepository.setFavoriteExcursion(excursion,profile)
    }
}