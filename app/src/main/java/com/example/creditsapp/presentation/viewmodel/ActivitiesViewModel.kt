package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.ActividadesRepository
import com.example.creditsapp.domain.model.Actividad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ActivitiesViewModel(
    private val actividadesRepository: ActividadesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ActividadesUiState>(ActividadesUiState.Loading)
    val uiState: StateFlow<ActividadesUiState> = _uiState

    private fun fetchActivities() {
        viewModelScope.launch {
            try {
                val actividades = actividadesRepository.getActividades()
                _uiState.value = ActividadesUiState.Success(actividades)
            } catch (e: IOException) {
                _uiState.value = ActividadesUiState.Error
            }
        }
    }

    fun getFilteredAndSortedActs(state: ActividadesUiState.Success): List<Actividad> {
        val filtered = state.actividades.filter { actividad ->
            val creditosOk = state.selectedCredits.isEmpty() || actividad.creditos in state.selectedCredits
            val tipoOk = state.selectedType == null || actividad.tipoActividad == state.selectedType
            creditosOk && tipoOk
        }

        return when (state.sortOption) {
            SortOption.NONE -> filtered
            SortOption.BY_NAME -> filtered.sortedBy { it.nombre }
            SortOption.BY_DATE -> filtered.sortedBy { it.fechaInicio }
        }
    }

    fun onSortOptionSelected(option: SortOption) {
        val currentState = _uiState.value
        if (currentState is ActividadesUiState.Success) {
            _uiState.value = currentState.copy(sortOption = option)
        }
    }

    fun onTypeFilter(type: Int) {
        _uiState.update { state ->
            if (state is ActividadesUiState.Success) {
                state.copy(selectedType = type)
            } else {
                state
            }
        }
    }

    fun onCreditToggle(credit: Double) {
        val currentState = _uiState.value
        if (currentState is ActividadesUiState.Success) {
            val updatedSet = currentState.selectedCredits.toggle(credit)
            _uiState.value = currentState.copy(selectedCredits = updatedSet)
        }
    }

    private fun Set<Double>.toggle(item: Double): Set<Double> {
        return if (this.contains(item)) this - item else this + item
    }

    init {
        fetchActivities()
    }
}

sealed interface ActividadesUiState {
    data class Success(
        val actividades: List<Actividad>,
        val sortOption: SortOption = SortOption.NONE,
        val selectedCredits: Set<Double> = emptySet(),
        val selectedType: Int? = null
    ) : ActividadesUiState

    object Error : ActividadesUiState
    object Loading : ActividadesUiState
}

enum class SortOption {
    NONE, BY_NAME, BY_DATE
}

