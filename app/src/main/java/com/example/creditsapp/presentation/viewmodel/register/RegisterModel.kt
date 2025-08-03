package com.example.creditsapp.presentation.viewmodel.register

import com.example.creditsapp.domain.model.Carrera
import com.example.creditsapp.domain.model.RegisterRequest

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val numeroControl: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val semestre: Int? = null,

    val isLoadingCareers: Boolean = false,
    val carreras: List<Carrera> = emptyList(),
    val errorCareers: String? = null,
    val selectedCareer: Carrera? = null,

    val isRegistering: Boolean = false,
    val registrationError: RegisterValidationErrorType? = null,
    val registrationSuccess: Boolean = false
)

sealed class RegisterIntent {
    data class EmailChanged(val value: String) : RegisterIntent()
    data class PasswordChanged(val value: String) : RegisterIntent()
    data class ConfirmPasswordChanged(val value: String) : RegisterIntent()
    data class NumeroControlChanged(val value: String) : RegisterIntent()
    data class NombreChanged(val value: String) : RegisterIntent()
    data class ApellidoChanged(val value: String) : RegisterIntent()
    data class SemestreChanged(val value: Int) : RegisterIntent()
    data class CareerSelected(val career: Carrera) : RegisterIntent()
    data object FetchCareers : RegisterIntent()
    data class CareersFetched(val carreras: List<Carrera>) : RegisterIntent()
    data class CareersFailed(val error: String?) : RegisterIntent()
    object Submit : RegisterIntent()
}

sealed class RegisterEffect {
    data class ShowSnackbar(val message: RegisterUiMessageEvent) : RegisterEffect()
    data class PerformRegister(val request: RegisterRequest) : RegisterEffect()
    object LoadCareers : RegisterEffect()
}


sealed class RegisterUiMessageEvent {
    data class ValidationError(val type: RegisterValidationErrorType) : RegisterUiMessageEvent()
    object RegistrationSuccess : RegisterUiMessageEvent()
    object RegistrationFailed : RegisterUiMessageEvent()
}

enum class RegisterValidationErrorType {
    EMPTY_NAME,
    EMPTY_EMAIL,
    EMPTY_NOCONTROL,
    INVALID_EMAIL,
    INVALID_SEMESTER,
    INVALID_CAREER,
    EMPTY_PASSWORD,
    PASSWORD_MISMATCH
}