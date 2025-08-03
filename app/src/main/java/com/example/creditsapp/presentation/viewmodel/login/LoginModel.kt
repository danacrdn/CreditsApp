package com.example.creditsapp.presentation.viewmodel.login

import com.example.creditsapp.domain.model.LoginRequest

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val loginSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: LoginValidationErrorType? = null
)

sealed class LoginIntent {
    data class UsernameChanged(val value: String) : LoginIntent()
    data class PasswordChanged(val value: String) : LoginIntent()
    object Submit : LoginIntent()
    object ResetLoginSuccess : LoginIntent()
}

sealed class LoginEffect {
    data class ShowSnackbar(val message: LoginUiMessageEvent) : LoginEffect()
    data class PerformLogin(val request: LoginRequest) : LoginEffect()
}

sealed class LoginUiMessageEvent {
    data class ValidationError(val type: LoginValidationErrorType) : LoginUiMessageEvent()
    object LoginSuccess : LoginUiMessageEvent()
    object LoginFailed : LoginUiMessageEvent()
}

sealed class LoginValidationErrorType {
    object EMPTY_EMAIL : LoginValidationErrorType()
    object EMPTY_PASSWORD : LoginValidationErrorType()
}