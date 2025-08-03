package com.example.creditsapp.presentation.viewmodel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AuthRepository
import com.example.creditsapp.data.repository.CarreraRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel(
    carreraRepository: CarreraRepository,
    authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    private val effectHandler =
        RegisterEffectHandler(
            authRepository,
            carreraRepository,
            viewModelScope,
            _uiState,
            _effect
        ) { intent -> onIntent(intent) }

    init {
        onIntent(RegisterIntent.FetchCareers)
    }

    fun onIntent(intent: RegisterIntent) {
        val (newState, sideEffect) = RegisterReducer.reduce(_uiState.value, intent)
        _uiState.value = newState
        sideEffect?.let { effectHandler.handle(it) }
    }
}
