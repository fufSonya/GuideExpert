package com.example.GuideExpert.domain.models

sealed class SnackbarEffect {
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : SnackbarEffect()
}