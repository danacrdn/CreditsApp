package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.repository.ActivitiesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ActivitiesViewModel(activitiesRepository: ActivitiesRepository) : ViewModel() {

    val activities: StateFlow<ActivitiesUiState> =
        activitiesRepository.getAllActivitiesStream().map { ActivitiesUiState(it) }
            .stateIn (
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ActivitiesUiState()
    )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class ActivitiesUiState(val activitiesList: List<Activity> = listOf())
