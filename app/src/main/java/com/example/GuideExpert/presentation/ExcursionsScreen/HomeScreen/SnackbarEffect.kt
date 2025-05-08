package com.example.GuideExpert.presentation.ExcursionsScreen.HomeScreen

sealed class SnackbarEffect {
    data class ShowSnackbar(val message: String, val actionLabel: String? = null) : SnackbarEffect()
}