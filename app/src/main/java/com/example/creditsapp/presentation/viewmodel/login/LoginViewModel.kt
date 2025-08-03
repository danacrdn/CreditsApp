package com.example.creditsapp.presentation.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AuthRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.domain.model.LoginRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    userPreferences: UserPreferencesRepository,
    authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    private val effectHandler = LoginEffectHandler(authRepository, userPreferences, viewModelScope, _uiState, _effect)

    fun onIntent(intent: LoginIntent) {
        val (newState, sideEffect) = LoginReducer.reduce(_uiState.value, intent)
        _uiState.value = newState
        sideEffect?.let { effectHandler.handle(it) }
    }
}
