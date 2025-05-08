package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Filter

interface GetFiltersBarUseCase {
    operator fun invoke() : List<Filter>
}