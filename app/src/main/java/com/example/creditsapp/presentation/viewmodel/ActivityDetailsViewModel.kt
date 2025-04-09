package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.repository.ActivitiesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ActivityDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    activitiesRepository: ActivitiesRepository
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
}

data class ActivityUiState(val activity: Activity? = null)