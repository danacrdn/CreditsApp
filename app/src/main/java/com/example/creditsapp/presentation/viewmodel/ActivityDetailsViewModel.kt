package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.database.UserActivity
import com.example.creditsapp.data.repository.ActivitiesRepository
import com.example.creditsapp.data.repository.UserActivitiesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivityDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val activitiesRepository: ActivitiesRepository,
    private val userActivitiesRepository: UserActivitiesRepository
) : ViewModel() {

    private val activityId: Int = checkNotNull(savedStateHandle["id"]) { "ID no encontrado" }

    val activityUiState: StateFlow<ActivityUiState> =
        activitiesRepository.getActivityStream(activityId)
            .map { activity -> ActivityUiState(activity = activity) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                initialValue = ActivityUiState()
            )

    fun insertActivityUser() {
        viewModelScope.launch {
            userActivitiesRepository.insertUserActivity(
                UserActivity(
                    activityId = activityId,
                    userId = 1,
                    completed = false
                )
            )
        }
    }
}

data class ActivityUiState(val activity: Activity? = null)