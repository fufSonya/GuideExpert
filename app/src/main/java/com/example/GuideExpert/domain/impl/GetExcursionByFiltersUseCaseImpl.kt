package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetExcursionByFiltersUseCase
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.domain.repository.ExcursionsRepository
import javax.inject.Inject

class GetExcursionByFiltersUseCaseImpl @Inject constructor(
    private val excursionsRepository: ExcursionsRepository
): GetExcursionByFiltersUseCase {
    override operator fun invoke(filter: Filters) = excursionsRepository.getExcursionByFiltersFlow(filter)
}