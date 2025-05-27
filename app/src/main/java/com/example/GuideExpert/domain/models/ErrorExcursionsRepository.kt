package com.example.GuideExpert.domain.models

sealed class ErrorExcursionsRepository {
    data object Authorization : ErrorExcursionsRepository()
    data object LoadingFavorites : ErrorExcursionsRepository()
}