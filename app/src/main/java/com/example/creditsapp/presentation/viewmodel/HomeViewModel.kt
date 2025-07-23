package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AlumnosRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val alumnosRepository: AlumnosRepository,
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.id.collectLatest { userId ->
                userId?.let {
                    fetchUser(it)
                }
            }
        }
    }

    private fun fetchUser(id: Int) {
        viewModelScope.launch {
            try {
                val result = alumnosRepository.getAlumnoById(id)
                println("Datos del alumno: $result")

                _uiState.value = _uiState.value.copy(
                    name = result.nombre,
                    totalCredits = result.totalCreditos
                )

            } catch (e: Exception) {
                println("Error al obtener datos: ${e.message}")
            }
        }
    }
}

data class HomeUiState(
    val name: String = "",
    val totalCredits: Double = 0.0,

    val isLoading: Boolean = true,
    val error: String? = null
)
