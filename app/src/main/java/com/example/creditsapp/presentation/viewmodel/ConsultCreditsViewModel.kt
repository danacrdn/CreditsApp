package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.repository.UserActivitiesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/* ViewModel para la pantalla de Mis Créditos.

*/

class ConsultCreditsViewModel (userActivitiesRepository: UserActivitiesRepository): ViewModel() {
    val id = 1
    val userCompletedActivities: StateFlow<UserCompletedActivitiesUiState> =
        userActivitiesRepository.getCompletedActivitiesForUserStream(id).map { UserCompletedActivitiesUiState(it) }
            .stateIn (
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UserCompletedActivitiesUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class UserCompletedActivitiesUiState(val userActivitiesList: List<Activity> = listOf())