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

class ConsultCreditsViewModel(
    private val userActivitiesRepository: UserActivitiesRepository,
    userPreferences: UserPreferencesRepository
) : ViewModel() {

    val id: StateFlow<Int?> = userPreferences.userId

    @OptIn(ExperimentalCoroutinesApi::class)
    val userCompletedActivities: StateFlow<UserCompletedActivitiesUiState> = id
        .filterNotNull()
        .flatMapLatest { id ->
            userActivitiesRepository.getCompletedActivitiesForUserStream(id)
                .map { UserCompletedActivitiesUiState(it) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = UserCompletedActivitiesUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class UserCompletedActivitiesUiState(val userActivitiesList: List<Activity> = listOf())