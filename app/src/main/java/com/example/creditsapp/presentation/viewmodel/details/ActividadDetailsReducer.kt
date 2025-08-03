package com.example.creditsapp.presentation.viewmodel.details

import com.example.creditsapp.presentation.utilities.UiState

object ActividadDetailsReducer {

    // función pura
    fun reducer(state: ActivityDetailsUiState, intent: ActivityDetailsIntent): Pair<ActivityDetailsUiState, ActivityDetailsEffect?> {
        return when (intent) {
            is ActivityDetailsIntent.LoadActivityDetails ->{
                state.copy(dataState = UiState.Loading) to null
            }

            is ActivityDetailsIntent.PerformAction -> {
                state.copy(isPerformingAction = true, errorMessage = null) to null
            }

            is ActivityDetailsIntent.DataLoaded -> {
                state.copy(
                    dataState = intent.result.fold(
                        onSuccess = { UiState.Success(it) },
                        onFailure = { UiState.Error }
                    )
                ) to null
            }
        }
    }
}