package com.example.creditsapp.presentation.viewmodel.details

import com.example.creditsapp.presentation.utilities.UiState

object ActividadDetailsReducer {

    fun reducer(state: ActivityDetailsUiState, intent: ActivityDetailsIntent): Pair<ActivityDetailsUiState, ActivityDetailsEffect?> {
        return when (intent) {
            is ActivityDetailsIntent.LoadActivityDetails ->{
                state.copy(dataState = UiState.Loading) to null
            }

            is ActivityDetailsIntent.PerformAction -> {
                state.copy(isPerformingAction = true, errorMessage = null) to null
            }
        }
    }
}