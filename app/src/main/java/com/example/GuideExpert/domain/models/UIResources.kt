package com.example.GuideExpert.domain.models

sealed class UIResources<out T> {
    data class Success<out T>(val data: T) : UIResources<T>()
    data class Error(val message: String) : UIResources<Nothing>()
    data object Loading : UIResources<Nothing>()
}