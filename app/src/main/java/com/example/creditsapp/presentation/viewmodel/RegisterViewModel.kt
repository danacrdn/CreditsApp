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

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

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
                        showError("Registro exitoso.")

                        _uiState.value = _uiState.value.copy(
                            isRegistering = false,
                            registrationSuccess = true
                        )

                    } catch (e: Exception) {
                        showError("Error al registrar nuevo usuario.")
                        println("Error al registrar nuevo usuario: ${e.message}")

                    }
                }
                is ValidationResult.Error -> {
                    viewModelScope.launch {
                        showError(validation.message)
                    }
                }
            }
        }
    }

    private suspend fun showError(message: String) {
        _uiState.value = _uiState.value.copy(isRegistering = false)
        _snackbarMessage.emit(message)
    }

    private fun validateForm(state: RegisterUiState): ValidationResult {
        return when {
            state.nombre.isBlank() || state.apellido.isBlank() ->
                ValidationResult.Error("Nombre y apellido son obligatorios.")

            state.email.isBlank() ->
                ValidationResult.Error("El correo no puede estar vacío.")
            state.numeroControl.isBlank() ->
                ValidationResult.Error("El número de control es obligatorio.")

            !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches() ->
                ValidationResult.Error("Formato de correo inválido.")

            state.semestre == null || state.semestre <= 0 ->
                ValidationResult.Error("Selecciona un semestre válido.")

            state.selectedCareer == null ->
                ValidationResult.Error("Selecciona una carrera.")
            state.password.isBlank() || state.confirmPassword.isBlank() ->
                ValidationResult.Error("La contraseña no puede estar vacía.")

            state.password != state.confirmPassword ->
                ValidationResult.Error("Las contraseñas no coinciden.")
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

    val isRegistering: Boolean = false, // Para mostrar un indicador de carga en el botón
    val registrationError: String? = null, // Para mostrar errores de registro
    val registrationSuccess: Boolean = false // Para indicar éxito
)

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
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
