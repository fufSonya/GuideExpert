package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.DeleteFavoriteExcursionUseCase
import com.example.GuideExpert.domain.models.DeleteFavoriteExcursionResponse
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteFavoriteExcursionUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): DeleteFavoriteExcursionUseCase {
    override suspend operator fun invoke(excursion: Excursion): Flow<UIResources<DeleteFavoriteExcursionResponse>> {
        return profileRepository.removeFavoriteExcursion(excursion)
    }
}