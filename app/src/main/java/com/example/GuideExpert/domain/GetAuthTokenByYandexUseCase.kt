package com.example.GuideExpert.domain

import com.example.GuideExpert.domain.models.ProfileYandex
import com.example.GuideExpert.domain.models.UIResources
import kotlinx.coroutines.flow.Flow

interface GetAuthTokenByYandexUseCase {
    suspend operator fun invoke(oauthToken: String) : Flow<UIResources<ProfileYandex>>
}