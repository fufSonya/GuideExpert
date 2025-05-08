package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionsFavoriteIdUseCase
import com.example.GuideExpert.domain.repository.ProfileRepository
import javax.inject.Inject

class GetExcursionsFavoriteIdUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository
): GetExcursionsFavoriteIdUseCase {
    override suspend operator fun invoke() {
        return profileRepository.getExcursionsFavorite()
    }
}