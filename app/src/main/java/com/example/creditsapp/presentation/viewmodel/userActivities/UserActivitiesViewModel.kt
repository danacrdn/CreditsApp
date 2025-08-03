package com.example.creditsapp.presentation.viewmodel.userActivities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.domain.model.CursoAlumno
import com.example.creditsapp.presentation.utilities.UiState
import com.example.creditsapp.presentation.utilities.fetchAsFlow
import com.example.creditsapp.presentation.utilities.toUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UserActivitiesViewModel(
    private val alumnoActividadRepository: AlumnoActividadRepository,
    userPreferences: UserPreferencesRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<List<CursoAlumno>>> = userPreferences.userId
        .filterNotNull()
        .flatMapLatest { id -> fetchAsFlow { alumnoActividadRepository.getActividadesPorAlumno(id) } }
        .map { result -> result.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Loading)
}
