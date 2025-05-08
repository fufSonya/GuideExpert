package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetFiltersSortUseCase
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.repository.DataProviderRepository
import javax.inject.Inject


class GetFiltersSortUseCaseImpl @Inject constructor(
    private val dataProviderRepository: DataProviderRepository
): GetFiltersSortUseCase {

    override operator fun invoke(): List<Filter> {
        return dataProviderRepository.getFiltersSort()
    }
}