package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.repository.UserActivitiesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UserActivitiesViewModel(userActivitiesRepository: UserActivitiesRepository) : ViewModel() {

    val id = 1
    val userActivities: StateFlow<UserActivitiesUiState> =
        userActivitiesRepository.getActivitiesForUserStream(id).map { UserActivitiesUiState(it) }
            .stateIn (
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UserActivitiesUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class UserActivitiesUiState(val userActivitiesList: List<Activity> = listOf())