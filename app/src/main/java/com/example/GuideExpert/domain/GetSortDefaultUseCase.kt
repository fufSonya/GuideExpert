package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.Filter

interface GetSortDefaultUseCase {
    operator fun invoke() : Int
}