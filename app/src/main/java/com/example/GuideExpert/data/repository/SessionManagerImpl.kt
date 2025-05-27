package com.example.GuideExpert.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.GuideExpert.domain.repository.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class SessionManagerImpl @Inject constructor(
    val context: Context
): SessionManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    private fun <T> read(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        context.dataStore.data.map { settings ->
            settings[key] ?: defaultValue
        }

    private val AUTH_TOKEN = stringPreferencesKey("auth_token")
    override suspend fun setAuthToken(value: String) = save(AUTH_TOKEN, value)

    override fun getAuthToken(): Flow<String> = read(AUTH_TOKEN, "")


    private val PROFILE_ID = stringPreferencesKey("profile_id")
    override suspend fun setProfileId(value: String) = save(PROFILE_ID, value)

    override fun getProfileId(): Flow<String> = read(PROFILE_ID, "")

    private val PROFILE_TIME = intPreferencesKey("profile_time")
    override suspend fun setProfileTime(value: Int) = save(PROFILE_TIME, value)

    fun getProfileTime(): Flow<Int> = read(PROFILE_TIME, 0)
}