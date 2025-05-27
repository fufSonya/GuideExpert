package com.example.GuideExpert.domain.impl

import com.example.GuideExpert.domain.GetConfigUseCase
import com.example.GuideExpert.domain.models.Config
import com.example.GuideExpert.domain.models.UIResources
import com.example.GuideExpert.domain.repository.DataSourceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConfigUseCaseImpl @Inject constructor(
    private val dataSourceRepository: DataSourceRepository
): GetConfigUseCase {
    override suspend operator fun invoke(): Flow<UIResources<Config>> {
        return dataSourceRepository.getConfigInfo()
    }
}
