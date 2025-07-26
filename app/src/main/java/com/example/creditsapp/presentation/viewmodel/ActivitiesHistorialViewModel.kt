package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.domain.model.CursoAlumno
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActivitiesHistorialViewModel(
    private val alumnoActividadRepository: AlumnoActividadRepository,
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ActivitiesHistorialUiState>(ActivitiesHistorialUiState.Loading)
    val uiState: StateFlow<ActivitiesHistorialUiState> = _uiState

    private fun fetchAlumnoActividadesCompletadas() {
        viewModelScope.launch {
            val alumnoId = userPreferences.userId.first()

            if (alumnoId != null) {
                runCatching { alumnoActividadRepository.getActividadesPorAlumno(alumnoId) }
                    .fold(
                        onSuccess = {
                            val actividadesCompletadas = it.filter { it.estadoAlumnoActividad == 4 } // 4 = acreditado
                            _uiState.value = ActivitiesHistorialUiState.Success(actividadesCompletadas)
                        },
                        onFailure = {
                            _uiState.value = ActivitiesHistorialUiState.Error
                        }
                    )
            } else {
                _uiState.value = ActivitiesHistorialUiState.Error
            }
        }
    }

    init {
        fetchAlumnoActividadesCompletadas()
    }

}

sealed interface ActivitiesHistorialUiState {
    data class Success(
        val actividades: List<CursoAlumno> = emptyList(),
    ) : ActivitiesHistorialUiState
    object Error : ActivitiesHistorialUiState
    object Loading : ActivitiesHistorialUiState
}