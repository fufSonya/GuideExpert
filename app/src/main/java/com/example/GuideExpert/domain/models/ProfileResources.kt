package com.example.GuideExpert.domain.models

sealed class ProfileResources {
    data object Success : ProfileResources()
    data class Error(val message: String) : ProfileResources()
    data object Loading : ProfileResources()
    data object Idle : ProfileResources()
}

