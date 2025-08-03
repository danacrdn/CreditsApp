package com.example.creditsapp.presentation.viewmodel.register

import com.example.creditsapp.domain.model.RegisterRequest
import com.example.creditsapp.presentation.utilities.ValidationResult

object RegisterReducer {
    fun reduce(
        state: RegisterUiState,
        intent: RegisterIntent
    ): Pair<RegisterUiState, RegisterEffect?> {
        return when (intent) {
            is RegisterIntent.ApellidoChanged ->
                state.copy(apellido = intent.value) to null

            is RegisterIntent.CareerSelected ->
                state.copy(selectedCareer = intent.career) to null

            is RegisterIntent.ConfirmPasswordChanged ->
                state.copy(confirmPassword = intent.value) to null

            is RegisterIntent.EmailChanged ->
                state.copy(email = intent.value) to null

            is RegisterIntent.NombreChanged -> state.copy(nombre = intent.value) to null
            is RegisterIntent.NumeroControlChanged -> state.copy(numeroControl = intent.value) to null
            is RegisterIntent.PasswordChanged -> state.copy(password = intent.value) to null
            is RegisterIntent.SemestreChanged -> state.copy(semestre = intent.value) to null

            RegisterIntent.Submit -> {
                when (val validation = validate(state)) {
                    is ValidationResult.Success -> {
                        val request = state.toRegisterRequest()
                        state.copy(isRegistering = true) to RegisterEffect.PerformRegister(request)
                    }

                    is ValidationResult.Error -> {
                        val errorType = validation.errorType
                        state.copy(registrationError = errorType) to RegisterEffect.ShowSnackbar(
                            RegisterUiMessageEvent.ValidationError(errorType)
                        )
                    }
                }
            }

            RegisterIntent.FetchCareers ->
                state.copy(
                    isLoadingCareers = true,
                    errorCareers = null
                ) to RegisterEffect.LoadCareers

            is RegisterIntent.CareersFailed ->
                state.copy(isLoadingCareers = false, errorCareers = intent.error) to null

            is RegisterIntent.CareersFetched ->
                state.copy(isLoadingCareers = false, carreras = intent.carreras) to null
        }
    }

    private fun validate(state: RegisterUiState): ValidationResult<RegisterValidationErrorType> {
        return when {
            state.nombre.isBlank() || state.apellido.isBlank() ->
                ValidationResult.Error(RegisterValidationErrorType.EMPTY_NAME)

            state.numeroControl.isBlank() ->
                ValidationResult.Error(RegisterValidationErrorType.EMPTY_NOCONTROL)

            state.email.isBlank() ->
                ValidationResult.Error(RegisterValidationErrorType.EMPTY_EMAIL)

            !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches() ->
                ValidationResult.Error(RegisterValidationErrorType.INVALID_EMAIL)

            state.semestre == null || state.semestre <= 0 ->
                ValidationResult.Error(RegisterValidationErrorType.INVALID_SEMESTER)

            state.selectedCareer == null ->
                ValidationResult.Error(RegisterValidationErrorType.INVALID_CAREER)

            state.password.isBlank() || state.confirmPassword.isBlank() ->
                ValidationResult.Error(RegisterValidationErrorType.EMPTY_PASSWORD)

            state.password != state.confirmPassword ->
                ValidationResult.Error(RegisterValidationErrorType.PASSWORD_MISMATCH)

            else -> ValidationResult.Success
        }
    }

    private fun RegisterUiState.toRegisterRequest(): RegisterRequest = RegisterRequest(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        numeroControl = numeroControl,
        nombre = nombre,
        apellido = apellido,
        semestre = semestre!!,
        totalCreditos = 0.0,
        carreraId = selectedCareer!!.id
    )
}