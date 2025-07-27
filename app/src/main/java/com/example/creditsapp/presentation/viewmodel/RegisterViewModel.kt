package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AuthRepository
import com.example.creditsapp.data.repository.CarreraRepository
import com.example.creditsapp.domain.model.Carrera
import com.example.creditsapp.domain.model.RegisterRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val carreraRepository: CarreraRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<RegisterUiMessageEvent>()
    val snackbarMessage: SharedFlow<RegisterUiMessageEvent> = _snackbarMessage

    init {
        fetchCareers()

    }

    private fun fetchCareers() {
        viewModelScope.launch {
            try {
                val result = carreraRepository.getCarreras()
                println("Carreras obtenidas desde la API: ${result.size}")
                _uiState.value = _uiState.value.copy(carreras = result)
            } catch (e: Exception) {
                println("Error al obtener carreras: ${e.message}")
                _uiState.value = _uiState.value.copy(errorCareers = e.message)
            }
        }
    }

    fun signUpNewUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isRegistering = true,
                registrationError = null,
                registrationSuccess = false
            )

            val state = _uiState.value
            when (val validation = validateForm(state)) {
                is ValidationResult.Success -> {
                    val registerRequest = RegisterRequest(
                        email = state.email,
                        password = state.password,
                        confirmPassword = state.confirmPassword,
                        numeroControl = state.numeroControl,
                        nombre = state.nombre,
                        apellido = state.apellido,
                        semestre = state.semestre!!,
                        totalCreditos = 0.0,
                        carreraId = state.selectedCareer!!.id
                    )

                    try {
                        println("Request: $registerRequest")
                        val result = authRepository.register(registerRequest)
                        println("Registro exitoso: $result")
                        _snackbarMessage.emit(RegisterUiMessageEvent.RegistrationSuccess)

                        _uiState.value = _uiState.value.copy(
                            isRegistering = false,
                            registrationSuccess = true
                        )

                    } catch (e: Exception) {
                        println("Error al registrar nuevo usuario: ${e.message}")
                        _snackbarMessage.emit(RegisterUiMessageEvent.RegistrationFailed)

                    }
                }

                is ValidationResult.Error -> {
                    viewModelScope.launch {
                        showError(validation.errorType)
                    }
                }
            }
        }
    }

    private suspend fun showError(errorType: RegisterValidationErrorType) {
        _uiState.value = _uiState.value.copy(isRegistering = false)
        _snackbarMessage.emit(RegisterUiMessageEvent.ValidationError(errorType))
    }

    private fun validateForm(state: RegisterUiState): ValidationResult<RegisterValidationErrorType> {
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


    fun onEvent(event: RegisterFormEvent) {
        _uiState.value = when (event) {
            is RegisterFormEvent.EmailChanged -> _uiState.value.copy(email = event.value)
            is RegisterFormEvent.PasswordChanged -> _uiState.value.copy(password = event.value)
            is RegisterFormEvent.ConfirmPasswordChanged -> _uiState.value.copy(confirmPassword = event.value)
            is RegisterFormEvent.NumeroControlChanged -> _uiState.value.copy(numeroControl = event.value)
            is RegisterFormEvent.NombreChanged -> _uiState.value.copy(nombre = event.value)
            is RegisterFormEvent.ApellidoChanged -> _uiState.value.copy(apellido = event.value)
            is RegisterFormEvent.SemestreChanged -> _uiState.value.copy(semestre = event.value)
            is RegisterFormEvent.CareerSelected -> _uiState.value.copy(selectedCareer = event.career)
        }
    }
}

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
    val registrationError: String? = null,
    val registrationSuccess: Boolean = false
)

sealed class ValidationResult<out T> {
    object Success : ValidationResult<Nothing>()
    data class Error<T>(val errorType: T) : ValidationResult<T>()
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

sealed class RegisterFormEvent {
    data class EmailChanged(val value: String) : RegisterFormEvent()
    data class PasswordChanged(val value: String) : RegisterFormEvent()
    data class ConfirmPasswordChanged(val value: String) : RegisterFormEvent()
    data class NumeroControlChanged(val value: String) : RegisterFormEvent()
    data class NombreChanged(val value: String) : RegisterFormEvent()
    data class ApellidoChanged(val value: String) : RegisterFormEvent()
    data class SemestreChanged(val value: Int) : RegisterFormEvent()
    data class CareerSelected(val career: Carrera) : RegisterFormEvent()
}

sealed class RegisterUiMessageEvent {
    data class ValidationError(val type: RegisterValidationErrorType) : RegisterUiMessageEvent()
    object RegistrationSuccess : RegisterUiMessageEvent()
    object RegistrationFailed : RegisterUiMessageEvent()
}
