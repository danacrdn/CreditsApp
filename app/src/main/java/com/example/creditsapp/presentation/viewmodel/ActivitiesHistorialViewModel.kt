package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.domain.model.CursoAlumno
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ActivitiesHistorialViewModel(
    private val alumnoActividadRepository: AlumnoActividadRepository,
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ActivitiesHistorialUiState>(ActivitiesHistorialUiState.Loading)
    val uiState: StateFlow<ActivitiesHistorialUiState> = _uiState

    private fun loadActividadesCompletadas() {
        viewModelScope.launch {
            getActividadesCompletadas().fold(
                onSuccess = { _uiState.value = ActivitiesHistorialUiState.Success(it) },
                onFailure = { _uiState.value = ActivitiesHistorialUiState.Error },
            )
        }
    }

    private suspend fun getActividadesCompletadas(): Result<List<CursoAlumno>> =
        userPreferences.userId.firstOrNull()?.let { id ->
            runCatching {
                alumnoActividadRepository.getActividadesPorAlumno(alumnoId = id).filter { it.estadoAlumnoActividad == 4 }
            }
        } ?: Result.failure(IllegalStateException("No hay se encontró el alumno asociado."))

    init {
        loadActividadesCompletadas()
    }
}

sealed interface ActivitiesHistorialUiState {
    data class Success(
        val actividades: List<CursoAlumno> = emptyList(),
    ) : ActivitiesHistorialUiState

    object Error : ActivitiesHistorialUiState
    object Loading : ActivitiesHistorialUiState
}