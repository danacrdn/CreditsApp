package com.example.creditsapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.repository.ActividadesRepository
import com.example.creditsapp.data.repository.ActivitiesRepository
import com.example.creditsapp.domain.model.Actividad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ActivitiesViewModel(
    private val activitiesRepository: ActivitiesRepository,
    private val actividadesRepository: ActividadesRepository
) : ViewModel() {

    private val _selectedCredits = MutableStateFlow<Set<Double>>(emptySet())
    val selectedCredits: StateFlow<Set<Double>> = _selectedCredits

    private val _sortOption = MutableStateFlow(SortOption.NONE)
    val sortOption: StateFlow<SortOption> = _sortOption

    var actividadesUiState: ActividadesUiState by mutableStateOf(ActividadesUiState.Loading)
        private set

    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjMwNDA1MTVjLWQyMzAtNGNjNS1iM2JlLWU1Yjg4NzY0OTQ3ZSIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6ImNvb3JkaW5hZG9yQGVqZW1wbG8uY29tIiwiaHR0cDovL3NjaGVtYXMubWljcm9zb2Z0LmNvbS93cy8yMDA4LzA2L2lkZW50aXR5L2NsYWltcy9yb2xlIjoiQ29vcmRpbmFkb3IiLCJleHAiOjE3NTIwMTc4NzIsImlzcyI6IlNpc3RlbWFDcmVkaXRvc0NvbXBsZW1lbnRhcmlvcyIsImF1ZCI6IlNpc3RlbWFDcmVkaXRvc0NvbXBsZW1lbnRhcmlvc1VzZXJzIn0.GCoEjiifnIZ9ZhiQEktbZhFjnwPmb1FHVHW-GtU3w8o"

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

    // lógica para obtener las actividades del backend

    private fun fetchActivities(){
        viewModelScope.launch {
            try {
                val actividades = actividadesRepository.getActividades()
                actividadesUiState = ActividadesUiState.Success(actividades)
            } catch (e: IOException){
                actividadesUiState = ActividadesUiState.Error
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    init {
        fetchActivities()
    }

}

sealed interface ActividadesUiState{
    data class Success(val actividades: List<Actividad>) : ActividadesUiState
    object Error : ActividadesUiState
    object Loading : ActividadesUiState
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

