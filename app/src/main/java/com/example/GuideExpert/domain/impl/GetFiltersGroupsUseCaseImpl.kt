package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetFiltersGroupsUseCase
import com.example.GuideExpert.domain.models.Filter
import com.example.GuideExpert.domain.repository.DataProviderRepository
import javax.inject.Inject


class GetFiltersGroupsUseCaseImpl @Inject constructor(
    private val dataProviderRepository: DataProviderRepository
): GetFiltersGroupsUseCase {
    override operator fun invoke(): List<Filter> {
        return dataProviderRepository.getFiltersGroups()
    }
}