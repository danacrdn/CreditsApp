package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel(private val userPreferences: UserPreferencesRepository) : ViewModel() {

    val userIdFlow: StateFlow<Int?> =
        userPreferences.userId.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isDarkMode: StateFlow<Boolean> =
        userPreferences.isDarkMode.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun switchDarkMode() {
        viewModelScope.launch {
            val current = isDarkMode.value
            userPreferences.saveDarkMode(!current)
        }
    }
}