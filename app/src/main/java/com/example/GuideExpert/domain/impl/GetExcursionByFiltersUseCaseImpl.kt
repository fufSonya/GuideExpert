package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionByFiltersUseCase
import com.example.GuideExpert.domain.models.FilterQuery
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.domain.repository.DataPagingRepository
import javax.inject.Inject

class GetExcursionByFiltersUseCaseImpl @Inject constructor(
    private val dataPagingRepository: DataPagingRepository
): GetExcursionByFiltersUseCase {
    override operator fun invoke(filter: Filters) = dataPagingRepository.getExcursionByFiltersFlow(filter)
}