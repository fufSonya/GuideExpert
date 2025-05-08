package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Filter

interface GetFiltersCategoriesUseCase {
    operator fun invoke() : List<Filter>
}