package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AuthRepository
import com.example.creditsapp.data.repository.CarreraRepository
import com.example.creditsapp.domain.model.Carrera
import com.example.creditsapp.domain.model.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val carreraRepository: CarreraRepository, private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    init {
        fetchCareers()

    }

    private fun fetchCareers(){
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

    fun signUpNewUser(){
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isRegistering = true,
                    registrationError = null,
                    registrationSuccess = false
                )
                val currentUiState = _uiState.value
                val selectedCareer = currentUiState.selectedCareer
                val selectedSemester = currentUiState.semestre
                if (selectedCareer == null || selectedSemester == null ) {
                    println("Error: No se ha seleccionado una carrera.")
                    return@launch
                }

                val registerRequest = RegisterRequest(
                    email = currentUiState.email,
                    password = currentUiState.password,
                    confirmPassword = currentUiState.confirmPassword,
                    numeroControl = currentUiState.numeroControl,
                    nombre = currentUiState.nombre,
                    apellido = currentUiState.apellido,
                    semestre = currentUiState.semestre,
                    totalCreditos = 0.0,
                    carreraId = currentUiState.selectedCareer.id
                )
                println("Request: $registerRequest")
                val result = authRepository.register(registerRequest)

                println("Registro exitoso: $result")
                _uiState.value = _uiState.value.copy(
                    isRegistering = false,
                    registrationSuccess = true
                )
            } catch (e: Exception) {
                println("Error al registrar nuevo usuario: ${e.message}")
            }
        }
    }

    fun onEmailChanged(newValue: String){
        _uiState.value = _uiState.value.copy(email = newValue)
    }

    fun onPasswordChanged(newValue: String){
        _uiState.value = _uiState.value.copy(password = newValue)
    }

    fun onConfirmPasswordChanged(newValue: String){
        _uiState.value = _uiState.value.copy(confirmPassword = newValue)
    }

    fun onNumeroControl(newValue: String){
        _uiState.value = _uiState.value.copy(numeroControl = newValue)
    }

    fun onNombreChanged(newValue: String){
        _uiState.value = _uiState.value.copy(nombre = newValue)
    }

    fun onApellidoChanged(newValue: String){
        _uiState.value = _uiState.value.copy(apellido = newValue)
    }

    fun onSemestreChanged(newValue: Int) {
        _uiState.value = _uiState.value.copy(semestre = newValue)
    }


    fun onCareerSelected(career: Carrera) {
        _uiState.value = _uiState.value.copy(selectedCareer = career)
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