package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionFavoriteUseCase
import com.example.GuideExpert.domain.repository.DataSourceRepository
import javax.inject.Inject

class GetExcursionFavoriteUseCaseImpl @Inject constructor(
    private val dataSourceRepository: DataSourceRepository
): GetExcursionFavoriteUseCase {
    override operator fun invoke() = dataSourceRepository.getExcursionFavoriteFlow()
}