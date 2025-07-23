package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.repository.AlumnosRepository
import com.example.creditsapp.data.repository.CarreraRepository
import com.example.creditsapp.domain.model.Alumno
import com.example.creditsapp.domain.model.AlumnoUpdate
import com.example.creditsapp.domain.model.Carrera
import com.example.creditsapp.domain.model.EditableProfileData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val alumnoRepository: AlumnosRepository,
    private val userPreferences: UserPreferencesRepository,
    private val carreraRepository: CarreraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val _navigateToLogin = MutableSharedFlow<Boolean>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    private fun fetchAlumnoData(id: Int) {
        viewModelScope.launch {
            try {
                val response = alumnoRepository.getAlumnoById(id)
                println("Datos del alumno: $response")

                val carreras = uiState.value.carreras
                val carrera = carreras.find { it.id == response.carreraId }

                _uiState.value = _uiState.value.copy(
                    profileData = response,
                    selectedCareer = carrera,
                    editableProfileData = EditableProfileData(
                        id = response.id,
                        nombre = response.nombre,
                        apellido = response.apellido,
                        semestre = response.semestre,
                        totalCreditos = response.totalCreditos,
                        carrera = carrera,
                        correoElectronico = response.correoElectronico,
                    )
                )
            } catch (e: Exception) {
                println("Error al obtener datos del alumno: ${e.message}")
            }
        }
    }

    init {
        viewModelScope.launch {
            loadCarreras()
            userPreferences.id.collectLatest { userId ->
                userId?.let {
                    fetchAlumnoData(it)
                }
            }
        }
    }

    /* This function activates editing mode. Once active, it assigns the original data
    * to observable value _editableData, which is of type EditableProfileData. This data class temporarily
    * stores the values before they are updated */
    fun startEditing() {
        val carreras = uiState.value.carreras
        val currentData = uiState.value.profileData
        if (currentData != null) {
            _uiState.value = _uiState.value.copy(
                isEditing = true,
                editableProfileData = currentData.toEditable(carreras)
            )
        }
    }

    private fun Alumno.toEditable(carreras: List<Carrera>): EditableProfileData {
        val carreraSeleccionada = carreras.find { it.id == this.carreraId }

        return EditableProfileData(
            id = this.id,
            nombre = this.nombre,
            apellido = this.apellido,
            semestre = this.semestre,
            totalCreditos = this.totalCreditos,
            carrera = carreraSeleccionada,
            correoElectronico = this.correoElectronico
        )
    }

    fun saveNewData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)

            val editableData = _uiState.value.editableProfileData
            println(editableData)

            if (editableData.id != null && editableData.semestre != null && editableData.carrera != null) {
                val updatedProfile = AlumnoUpdate(
                    id = editableData.id,
                    nombre = editableData.nombre,
                    apellido = editableData.apellido,
                    semestre = editableData.semestre,
                    totalCreditos = editableData.totalCreditos,
                    carreraId = editableData.carrera.id,
                    correoElectronico = editableData.correoElectronico,
                    currentPassword = editableData.currentPassword,
                    newPassword = editableData.newPassword
                )

                try {
                    val result = alumnoRepository.updateAlumno(updatedProfile.id, updatedProfile)
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        isEditing = false,
                        profileData = result
                    )
                } catch (e: Exception) {
                    println("No se pudo actualizar el alumno $e")
                }
            }
        }
    }

    /* This function cancels all the changes and turns off the editing mode.*/
    fun cancelEditing() {
        _uiState.value = _uiState.value.copy(isEditing = false)
    }

    fun logOut() {
        viewModelScope.launch {
            userPreferences.removeUserId()
            _navigateToLogin.emit(true)
        }
    }

    fun onEvent(event: ProfileEditEvent) {
        _uiState.value = when (event) {
            is ProfileEditEvent.NombreChanged -> _uiState.value.copy(
                editableProfileData = _uiState.value.editableProfileData.copy(nombre = event.value)
            )

            is ProfileEditEvent.ApellidoChanged -> _uiState.value.copy(
                editableProfileData = _uiState.value.editableProfileData.copy(apellido = event.value)
            )

            is ProfileEditEvent.CarreraChanged -> _uiState.value.copy(
                editableProfileData = _uiState.value.editableProfileData.copy(carrera = event.value)
            )

            is ProfileEditEvent.ConfirmPasswordChanged -> _uiState.value.copy(
                editableProfileData = _uiState.value.editableProfileData.copy(newPassword = event.value)
            )

            is ProfileEditEvent.EmailChanged -> _uiState.value.copy(
                editableProfileData = _uiState.value.editableProfileData.copy(correoElectronico = event.value)
            )

            is ProfileEditEvent.PasswordChanged -> _uiState.value.copy(
                editableProfileData = _uiState.value.editableProfileData.copy(currentPassword = event.value)
            )

            is ProfileEditEvent.SemestreChanged -> _uiState.value.copy(
                editableProfileData = _uiState.value.editableProfileData.copy(semestre = event.value)
            )
        }
    }

    private suspend fun loadCarreras() {

        try {
            val carreras = carreraRepository.getCarreras()
            _uiState.value = _uiState.value.copy(carreras = carreras)
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(errorCareers = "Error al cargar carreras")
        }

    }
}

data class ProfileUiState(
    val editableProfileData: EditableProfileData = EditableProfileData(),
    val profileData: Alumno? = null,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,

    val carreras: List<Carrera> = emptyList(),
    val errorCareers: String? = null,
    val selectedCareer: Carrera? = null,
)

sealed class ProfileEditEvent {
    data class NombreChanged(val value: String) : ProfileEditEvent()
    data class ApellidoChanged(val value: String) : ProfileEditEvent()
    data class SemestreChanged(val value: Int) : ProfileEditEvent()
    data class CarreraChanged(val value: Carrera) : ProfileEditEvent()
    data class EmailChanged(val value: String) : ProfileEditEvent()
    data class PasswordChanged(val value: String) : ProfileEditEvent()
    data class ConfirmPasswordChanged(val value: String) : ProfileEditEvent()
}