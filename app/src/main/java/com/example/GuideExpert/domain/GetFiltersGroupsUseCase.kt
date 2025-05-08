package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Filter

interface GetFiltersGroupsUseCase {
    operator fun invoke() : List<Filter>
}