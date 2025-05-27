package com.example.GuideExpert.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionManager {
    fun getProfileId(): Flow<String>
    fun getAuthToken(): Flow<String>
    suspend fun setAuthToken(value: String)
    suspend fun setProfileId(value: String)
    suspend fun setProfileTime(value: Int)
}