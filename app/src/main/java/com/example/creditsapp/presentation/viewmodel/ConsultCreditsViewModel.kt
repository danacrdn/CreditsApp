package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.data.repository.UserActivitiesRepository
import com.example.creditsapp.domain.model.Actividad
import com.example.creditsapp.domain.model.AlumnoActividad
import com.example.creditsapp.domain.model.CursoAlumno
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConsultCreditsViewModel(
    private val alumnoActividadRepository: AlumnoActividadRepository,
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConsultCreditsUiState>(ConsultCreditsUiState.Loading)
    val uiState: StateFlow<ConsultCreditsUiState> = _uiState

    private fun fetchAlumnoActividadesCompletadas() {
        viewModelScope.launch {
            val alumnoId = userPreferences.userId.first()

            if (alumnoId != null) {
                runCatching { alumnoActividadRepository.getActividadesPorAlumno(alumnoId) }
                    .fold(
                        onSuccess = {
                            val actividadesCompletadas = it.filter { it.estadoAlumnoActividad == 4 } // 4 = acreditado
                            _uiState.value = ConsultCreditsUiState.Success(actividadesCompletadas)
                        },
                        onFailure = {
                            _uiState.value = ConsultCreditsUiState.Error
                        }
                    )
            } else {
                _uiState.value = ConsultCreditsUiState.Error
            }
        }
    }

    init {
        fetchAlumnoActividadesCompletadas()
    }

}

sealed interface ConsultCreditsUiState {
    data class Success(
        val actividades: List<CursoAlumno> = emptyList(),
    ) : ConsultCreditsUiState
    object Error : ConsultCreditsUiState
    object Loading : ConsultCreditsUiState
}