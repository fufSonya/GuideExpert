package com.example.GuideExpert.domain

import androidx.paging.PagingData
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.Filters
import kotlinx.coroutines.flow.Flow

interface GetExcursionByFiltersUseCase {
    operator fun invoke(filter: Filters): Flow<PagingData<Excursion>>
}