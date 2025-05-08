package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetFiltersCategoriesUseCase
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.repository.DataProviderRepository
import javax.inject.Inject

class GetFiltersCategoriesUseCaseImpl @Inject constructor(
    private val dataProviderRepository: DataProviderRepository
): GetFiltersCategoriesUseCase {
    override operator fun invoke(): List<Filter> {
        return dataProviderRepository.getFiltersCategories()
    }
}