package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Filter

interface GetFiltersSortUseCase {
    operator fun invoke() : List<Filter>
}