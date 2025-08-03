package com.example.creditsapp.presentation.viewmodel.login

import com.example.creditsapp.domain.model.LoginRequest
import com.example.creditsapp.presentation.utilities.ValidationResult
import com.example.creditsapp.presentation.utilities.chainValidations
import com.example.creditsapp.presentation.utilities.toValidationResult

object LoginReducer {

    // funciones pura
    fun reduce(state: LoginUiState, intent: LoginIntent): Pair<LoginUiState, LoginEffect?> {
        return when (intent) {
            is LoginIntent.UsernameChanged ->
                state.copy(username = intent.value, error = null) to null

            is LoginIntent.PasswordChanged ->
                state.copy(password = intent.value, error = null) to null

            is LoginIntent.Submit -> {
                when (val validation = validate(state)) {
                    is ValidationResult.Success -> {
                        val request = state.toLoginRequest()
                        state.copy(isLoading = true) to LoginEffect.PerformLogin(request)
                    }

                    is ValidationResult.Error -> {
                        val errorType = validation.errorType
                        state.copy(error = errorType) to LoginEffect.ShowSnackbar(
                            LoginUiMessageEvent.ValidationError(errorType)
                        )
                    }
                }
            }

            is LoginIntent.ResetLoginSuccess -> state.copy(loginSuccess = false) to null
        }
    }

    private fun validate(state: LoginUiState): ValidationResult<LoginValidationErrorType> =
        listOf(
            { state.username.isNotBlank().toValidationResult(LoginValidationErrorType.EMPTY_EMAIL) },
            { state.password.isNotBlank().toValidationResult(LoginValidationErrorType.EMPTY_PASSWORD) }
        ).chainValidations()

    private fun LoginUiState.toLoginRequest() =
        LoginRequest(usuario = username, password = password)
}

