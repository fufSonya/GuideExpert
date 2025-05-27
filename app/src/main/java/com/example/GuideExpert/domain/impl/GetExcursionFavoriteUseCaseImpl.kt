package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionFavoriteUseCase
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import javax.inject.Inject

class GetExcursionFavoriteUseCaseImpl @Inject constructor(
    private val excursionsRepository: ExcursionsRepository
): GetExcursionFavoriteUseCase {
    override operator fun invoke() = excursionsRepository.getExcursionFavoriteFlow()
}