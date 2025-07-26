package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.domain.model.CursoAlumno
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserActivitiesViewModel(
    private val alumnoActividadRepository: AlumnoActividadRepository,
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserActivitiesUiState>(UserActivitiesUiState.Loading)
    val uiState: StateFlow<UserActivitiesUiState> = _uiState

    private fun fetchActividades() {
        viewModelScope.launch {
            val alumnoId = userPreferences.userId.first()

            if (alumnoId != null) {
                runCatching { alumnoActividadRepository.getActividadesPorAlumno(alumnoId) }
                    .fold(
                        onSuccess = {
                            _uiState.value = UserActivitiesUiState.Success(it)
                        },
                        onFailure = {
                            _uiState.value = UserActivitiesUiState.Error
                        }
                    )
            } else {
                _uiState.value = UserActivitiesUiState.Error
            }
        }
    }
    init {
        fetchActividades()
    }

}

sealed interface UserActivitiesUiState {
    data class Success(
        val actividades: List<CursoAlumno>,
    ) : UserActivitiesUiState

    data object Error : UserActivitiesUiState
    data object Loading : UserActivitiesUiState
}
