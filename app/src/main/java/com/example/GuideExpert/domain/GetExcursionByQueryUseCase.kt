package com.example.GuideExpert.domain

import androidx.paging.PagingData
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.FilterQuery
import kotlinx.coroutines.flow.Flow

interface GetExcursionByQueryUseCase {
    operator fun invoke(filter: FilterQuery): Flow<PagingData<Excursion>>
}