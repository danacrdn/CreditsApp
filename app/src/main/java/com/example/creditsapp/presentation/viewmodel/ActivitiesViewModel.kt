package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.repository.ActivitiesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ActivitiesViewModel(
    private val activitiesRepository: ActivitiesRepository
) : ViewModel() {

    private val _selectedCredits = MutableStateFlow<Set<Double>>(emptySet())
    val selectedCredits: StateFlow<Set<Double>> = _selectedCredits

    private val _sortOption = MutableStateFlow(SortOption.NONE)
    val sortOption: StateFlow<SortOption> = _sortOption


    val activities: StateFlow<ActivitiesUiState> =
        combine(
            activitiesRepository.getAllActivitiesStream(),
            selectedCredits,
            sortOption
        ) { activities, credits, sortOption ->

            val filtered = if (credits.isEmpty()) {
                activities
            } else {
                activities.filter { it.value in credits }
            }

            val sorted = when (sortOption) {
                SortOption.BY_DATE -> filtered.sortedBy { it.date }
                SortOption.BY_NAME -> filtered.sortedBy { it.name }
                SortOption.NONE -> filtered
            }

            ActivitiesUiState(sorted)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ActivitiesUiState()
        )

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun toggleCreditFilter(credit: Double) {
        _selectedCredits.update { credits ->
            if (credits.contains(credit)) {
                credits - credit
            } else {
                credits + credit
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class ActivitiesUiState(val activitiesList: List<Activity> = listOf())


enum class SortOption {
    NONE, BY_NAME, BY_DATE
}

/*

val activities: StateFlow<ActivitiesUiState> =
        activitiesRepository.getAllActivitiesStream().map { ActivitiesUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ActivitiesUiState()
            )

 */

