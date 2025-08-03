package com.example.creditsapp.presentation.viewmodel.register

import android.util.Patterns
import com.example.creditsapp.domain.model.RegisterRequest
import com.example.creditsapp.presentation.utilities.ValidationResult
import com.example.creditsapp.presentation.utilities.chainValidations
import com.example.creditsapp.presentation.utilities.toValidationResult

// función pura
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

    private fun validate(state: RegisterUiState): ValidationResult<RegisterValidationErrorType> =
        listOf(
            { (state.nombre.isNotBlank() && state.apellido.isNotBlank()).toValidationResult(RegisterValidationErrorType.EMPTY_NAME) },
            { state.numeroControl.isNotBlank().toValidationResult(RegisterValidationErrorType.EMPTY_NOCONTROL) },
            { state.email.isNotBlank().toValidationResult(RegisterValidationErrorType.EMPTY_EMAIL) },
            { Patterns.EMAIL_ADDRESS.matcher(state.email).matches().toValidationResult(RegisterValidationErrorType.INVALID_EMAIL) },
            { (state.semestre != null && state.semestre > 0).toValidationResult(RegisterValidationErrorType.INVALID_SEMESTER) },
            { (state.selectedCareer != null).toValidationResult(RegisterValidationErrorType.INVALID_CAREER) },
            { (state.password.isNotBlank() && state.confirmPassword.isNotBlank()).toValidationResult(RegisterValidationErrorType.EMPTY_PASSWORD) },
            { (state.password == state.confirmPassword).toValidationResult(RegisterValidationErrorType.PASSWORD_MISMATCH) }
        ).chainValidations()

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