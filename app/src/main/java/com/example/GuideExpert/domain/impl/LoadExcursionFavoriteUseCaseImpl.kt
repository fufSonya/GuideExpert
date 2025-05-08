package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.data.repository.UIResources
import com.example.GuideExpert.domain.LoadExcursionFavoriteUseCase
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadExcursionFavoriteUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): LoadExcursionFavoriteUseCase {
    override suspend operator fun invoke(): Flow<UIResources<List<Excursion>?>> {
        return profileRepository.fetchExcursionsFavorite()
    }
}