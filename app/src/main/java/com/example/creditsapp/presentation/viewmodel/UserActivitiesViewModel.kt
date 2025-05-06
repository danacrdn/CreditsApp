package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.repository.UserActivitiesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UserActivitiesViewModel(
    private val userActivitiesRepository: UserActivitiesRepository,
    userPreferences: UserPreferencesRepository
) : ViewModel() {

    val id: StateFlow<Int?> = userPreferences.userId

    @OptIn(ExperimentalCoroutinesApi::class)
    val userActivities: StateFlow<UserActivitiesUiState> = id
        .filterNotNull()
        .flatMapLatest { id ->
            userActivitiesRepository.getActivitiesForUserStream(id)
                .map { UserActivitiesUiState(it) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = UserActivitiesUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class UserActivitiesUiState(val userActivitiesList: List<Activity> = listOf())