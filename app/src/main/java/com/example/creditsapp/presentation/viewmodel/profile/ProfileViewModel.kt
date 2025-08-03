package com.example.creditsapp.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AlumnosRepository
import com.example.creditsapp.data.repository.CarreraRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    alumnoRepository: AlumnosRepository,
    private val userPreferences: UserPreferencesRepository,
    carreraRepository: CarreraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect = _effect.asSharedFlow()

    private val effectHandler = ProfileEffectHandler(
        alumnoRepository,
        carreraRepository,
        userPreferences,
        viewModelScope,
        _uiState,
        _effect
    )

    init {
        viewModelScope.launch {
            effectHandler.handle(ProfileEffect.FetchCarreras)
            userPreferences.id.collectLatest { id ->
                val nextEffect = if (id != null) {
                    ProfileEffect.FetchAlumnoData(id)
                } else {
                    ProfileEffect.PerformLogout
                }
                effectHandler.handle(nextEffect)
            }
        }
    }

    fun onIntent(intent: ProfileIntent) {
        val (newState, sideEffect) = ProfileReducer.reduce(_uiState.value, intent)
        _uiState.value = newState
        sideEffect?.let { effectHandler.handle(it) }
    }
}

