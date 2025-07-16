package com.example.creditsapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.creditsapp.data.repository.ActividadesRepository
import com.example.creditsapp.data.repository.AlumnosRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.repository.UsersRepository
import com.example.creditsapp.domain.model.Actividad
import com.example.creditsapp.domain.model.Alumno
import com.example.creditsapp.domain.model.Post
import com.example.creditsapp.domain.model.UserTotalCredits
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Thread.State

class HomeViewModel(
    private val usersRepository: UsersRepository,
    private val alumnosRepository: AlumnosRepository,
    private val actividadesRepository: ActividadesRepository,
    userPreferences: UserPreferencesRepository
) : ViewModel() {

    val id: StateFlow<Int?> = userPreferences.userId
    private val _alumnos = MutableStateFlow<List<Alumno>>(emptyList())
    val alumnos: StateFlow<List<Alumno>> = _alumnos.asStateFlow()

    private val _acts = MutableStateFlow<List<Actividad>>(emptyList())
    val acts: StateFlow<List<Actividad>> = _acts.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val homeUiState: StateFlow<HomeUiState> = id
        .filterNotNull()
        .flatMapLatest { id -> usersRepository.getUserAndCredits(id).map { HomeUiState(it) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState(UserTotalCredits("", 0))
        )

    fun fetchAlumnos() {
        viewModelScope.launch {
            try {
                // Esta llamada es la que activa el Log en tu repositorio
                val fetchedAlumnos = alumnosRepository.getAlumnos()
                _alumnos.value = fetchedAlumnos
                Log.d("MyViewModel", "Alumnos cargados en ViewModel: ${fetchedAlumnos.size} elementos")
            } catch (e: Exception) {
                Log.e("MyViewModel", "Error al cargar alumnos en ViewModel: ${e.message}", e)
            }
        }
    }

    /*
    fun fetchActividades() {
        viewModelScope.launch {
            Log.d("TOKEN_DEBUG", "Token: $token")
            val fetchedActivities = actividadesRepository.getActividades(token)
            _acts.value = fetchedActivities
            Log.d("MyViewModel", "Actividades cargados en ViewModel: ${fetchedActivities.size} elementos")
        }
    }
    */

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val userCredits: UserTotalCredits)
