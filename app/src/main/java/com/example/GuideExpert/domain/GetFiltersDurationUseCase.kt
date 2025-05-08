package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Filter

interface GetFiltersDurationUseCase {
    operator fun invoke() : List<Filter>
}