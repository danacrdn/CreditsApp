package com.example.creditsapp.presentation.viewmodel.profile

import com.example.creditsapp.domain.model.Alumno
import com.example.creditsapp.domain.model.AlumnoUpdate
import com.example.creditsapp.domain.model.Carrera
import com.example.creditsapp.domain.model.EditableProfileData

object ProfileReducer {

    // función pura
    fun reduce(state: ProfileUiState, intent: ProfileIntent) : Pair<ProfileUiState, ProfileEffect?> {
        return when (intent) {
            ProfileIntent.LoadData -> state to null

            ProfileIntent.LoadCarreras -> state to ProfileEffect.FetchCarreras

            is ProfileIntent.NombreChanged ->
                state.copy(editableProfileData = state.editableProfileData.copy(nombre = intent.value)) to null

            is ProfileIntent.ApellidoChanged ->
                state.copy(editableProfileData = state.editableProfileData.copy(apellido = intent.value)) to null

            is ProfileIntent.SemestreChanged ->
                state.copy(editableProfileData = state.editableProfileData.copy(semestre = intent.value)) to null

            is ProfileIntent.CarreraChanged ->
                state.copy(editableProfileData = state.editableProfileData.copy(carrera = intent.value)) to null

            is ProfileIntent.EmailChanged ->
                state.copy(editableProfileData = state.editableProfileData.copy(correoElectronico = intent.value)) to null

            is ProfileIntent.PasswordChanged ->
                state.copy(editableProfileData = state.editableProfileData.copy(currentPassword = intent.value)) to null

            is ProfileIntent.ConfirmPasswordChanged ->
                state.copy(editableProfileData = state.editableProfileData.copy(newPassword = intent.value)) to null

            ProfileIntent.StartEditing -> {
                val currentData = state.profileData
                if (currentData != null) {
                    state.copy(
                        isEditing = true,
                        editableProfileData = currentData.toEditable(state.carreras)
                    ) to null
                } else state to null
            }

            ProfileIntent.CancelEditing ->
                state.copy(isEditing = false) to null

            ProfileIntent.SaveData -> {
                val ed = state.editableProfileData
                if (ed.id != null && ed.semestre != null && ed.carrera != null) {
                    state.copy(isSaving = true) to ProfileEffect.SaveAlumnoUpdate(
                        AlumnoUpdate(
                            id = ed.id,
                            nombre = ed.nombre,
                            apellido = ed.apellido,
                            semestre = ed.semestre,
                            totalCreditos = ed.totalCreditos,
                            carreraId = ed.carrera.id,
                            correoElectronico = ed.correoElectronico,
                            currentPassword = ed.currentPassword,
                            newPassword = ed.newPassword
                        )
                    )

                } else {
                    state.copy(errorMessage = "Datos incompletos") to null
                }
            }

            ProfileIntent.LogOut -> state to ProfileEffect.PerformLogout
        }
    }

    fun Alumno.toEditable(carreras: List<Carrera>): EditableProfileData {
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
}