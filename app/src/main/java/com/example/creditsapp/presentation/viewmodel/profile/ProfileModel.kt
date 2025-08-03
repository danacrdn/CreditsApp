package com.example.creditsapp.presentation.viewmodel.profile

import com.example.creditsapp.domain.model.Alumno
import com.example.creditsapp.domain.model.AlumnoUpdate
import com.example.creditsapp.domain.model.Carrera
import com.example.creditsapp.domain.model.EditableProfileData


data class ProfileUiState(
    val editableProfileData: EditableProfileData = EditableProfileData(),
    val profileData: Alumno? = null,

    val isSaving: Boolean = false,
    val isEditing: Boolean = false,

    val carreras: List<Carrera> = emptyList(),
    val errorCareers: String? = null,
    val selectedCareer: Carrera? = null,

    val navigateToLogin: Boolean = false,
    val errorMessage: String? = null
)

sealed class ProfileIntent {
    object LoadData : ProfileIntent()
    object LoadCarreras : ProfileIntent()

    data class NombreChanged(val value: String) : ProfileIntent()
    data class ApellidoChanged(val value: String) : ProfileIntent()
    data class SemestreChanged(val value: Int) : ProfileIntent()
    data class CarreraChanged(val value: Carrera) : ProfileIntent()
    data class EmailChanged(val value: String) : ProfileIntent()
    data class PasswordChanged(val value: String) : ProfileIntent()
    data class ConfirmPasswordChanged(val value: String) : ProfileIntent()

    object StartEditing : ProfileIntent()
    object CancelEditing : ProfileIntent()
    object SaveData : ProfileIntent()
    object LogOut : ProfileIntent()
}

sealed class ProfileEffect {
    data class FetchAlumnoData(val id: Int) : ProfileEffect()
    object FetchCarreras : ProfileEffect()
    data class SaveAlumnoUpdate(val update: AlumnoUpdate) : ProfileEffect()
    object PerformLogout : ProfileEffect()
    data class ShowSnackbar(val message: ProfileUiMessageEvent) : ProfileEffect()
}

sealed class ProfileUiMessageEvent {
    object UpdateSuccess : ProfileUiMessageEvent()
    object UpdateFailed : ProfileUiMessageEvent()
    object FetchFailed : ProfileUiMessageEvent()
}
