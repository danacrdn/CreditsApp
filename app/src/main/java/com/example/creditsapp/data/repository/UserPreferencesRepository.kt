package com.example.creditsapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>, appScope: CoroutineScope) {
    private companion object {
        val USER_ID_KEY = intPreferencesKey("user_id")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    val id: Flow<Int?> = dataStore.data
        .map { prefs -> prefs[USER_ID_KEY] }


    val userId: StateFlow<Int?> = dataStore.data
        .map { prefs -> prefs[USER_ID_KEY] }
        .stateIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { prefs -> prefs[USER_ID_KEY] = userId }
    }

    suspend fun removeUserId() {
        dataStore.edit { prefs -> prefs.remove(USER_ID_KEY) }
    }

    val isDarkMode: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_DARK_MODE] ?: false
    }

    suspend fun saveDarkMode(isDarkMode: Boolean) {
        dataStore.edit { prefs -> prefs[IS_DARK_MODE] = isDarkMode }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")