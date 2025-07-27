package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AuthRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.domain.model.LoginRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPreferences: UserPreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<LoginUiMessageEvent>()
    val snackbarMessage: SharedFlow<LoginUiMessageEvent> = _snackbarMessage

    fun onEvent(event: LoginFormEvent) {
        _uiState.value = when (event) {
            is LoginFormEvent.UsernameChanged-> _uiState.value.copy(username = event.value)
            is LoginFormEvent.PasswordChanged -> _uiState.value.copy(password = event.value)
        }
    }

    fun logIn(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loginError = null,
                loginSuccess = false
            )

            val state = _uiState.value
            when(val validation = validateForm(state)) {
                is ValidationResult.Success -> {
                    val loginRequest = LoginRequest(
                        usuario = state.username,
                        password = state.password
                    )

                    try {
                        val result = authRepository.login(loginRequest)
                        println("Inicio de sesión exitoso: $result")

                        // save the user or alumno id
                        userPreferences.saveUserId(result.alumnoId)

                        _snackbarMessage.emit(LoginUiMessageEvent.LoginSuccess)

                        _uiState.value = _uiState.value.copy(
                            loginSuccess = true
                        )

                    } catch (e: Exception) {
                        println("Error al iniciar sesión: ${e.message}")
                        _snackbarMessage.emit(LoginUiMessageEvent.LoginFailed)
                    }
                }

                is ValidationResult.Error -> {
                    val errorType = validation.errorType
                    _snackbarMessage.emit(LoginUiMessageEvent.ValidationError(errorType))
                }
            }
        }
    }

    private fun validateForm(state: LoginUiState): ValidationResult<LoginValidationErrorType> {
        return when {
            state.username.isBlank() -> ValidationResult.Error(LoginValidationErrorType.EMPTY_EMAIL)
            state.password.isBlank() -> ValidationResult.Error(LoginValidationErrorType.EMPTY_PASSWORD)
            else -> ValidationResult.Success
        }
    }

    fun resetLoginSuccess() {
        _uiState.value = _uiState.value.copy(loginSuccess = false)
    }
}

sealed class LoginUiMessageEvent {
    data class ValidationError(val type: LoginValidationErrorType) : LoginUiMessageEvent()
    object LoginSuccess : LoginUiMessageEvent()
    object LoginFailed : LoginUiMessageEvent()
}

sealed class LoginFormEvent {
    data class UsernameChanged(val value: String) : LoginFormEvent()
    data class PasswordChanged(val value: String) : LoginFormEvent()
}

sealed class LoginValidationErrorType {
    object EMPTY_EMAIL : LoginValidationErrorType()
    object EMPTY_PASSWORD : LoginValidationErrorType()
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",

    val loginError: String? = null,
    val loginSuccess: Boolean = false
)