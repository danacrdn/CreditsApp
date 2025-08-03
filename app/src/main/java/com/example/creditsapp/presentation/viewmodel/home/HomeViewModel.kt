package com.example.creditsapp.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AlumnosRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.domain.model.Alumno
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

class HomeViewModel(
    private val alumnosRepository: AlumnosRepository,
    userPreferences: UserPreferencesRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<Alumno>> = userPreferences.userId
        .filterNotNull()
        .flatMapLatest { id -> fetchAsFlow { alumnosRepository.getAlumnoById(id) } }
        .map { result -> result.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UiState.Loading
        )
}
