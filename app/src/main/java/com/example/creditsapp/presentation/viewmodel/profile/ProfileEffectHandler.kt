package com.example.creditsapp.presentation.viewmodel.profile

import com.example.creditsapp.data.repository.AlumnosRepository
import com.example.creditsapp.data.repository.CarreraRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.presentation.viewmodel.profile.ProfileReducer.toEditable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileEffectHandler(
    private val alumnoRepository: AlumnosRepository,
    private val carreraRepository: CarreraRepository,
    private val userPreferences: UserPreferencesRepository,
    private val scope: CoroutineScope,
    private val state: MutableStateFlow<ProfileUiState>,
    private val effectFlow: MutableSharedFlow<ProfileEffect>
) {
    fun handle(effect: ProfileEffect) = when (effect) {
        is ProfileEffect.FetchAlumnoData -> handleFetchAlumno(effect)
        is ProfileEffect.FetchCarreras -> handleFetchCarreras()
        is ProfileEffect.SaveAlumnoUpdate -> handleAlumnoUpdate(effect)
        ProfileEffect.PerformLogout -> handleLogout()
        else -> Unit
    }

    private fun handleFetchAlumno(effect: ProfileEffect.FetchAlumnoData) = launchInScope {
        runCatching {
            val alumno = alumnoRepository.getAlumnoById(effect.id)
            val carrera = state.value.carreras.find { it.id == alumno.carreraId }
            alumno.toEditable(state.value.carreras).let { editable ->
                state.update {
                    it.copy(
                        profileData = alumno,
                        selectedCareer = carrera,
                        editableProfileData = editable
                    )
                }
            }
        }.onFailure {
            state.update { it.copy(errorMessage = "Error al obtener datos del alumno") }
        }
    }

    private fun handleFetchCarreras() = launchInScope {
        runCatching {
            carreraRepository.getCarreras()
        }.onSuccess { carreras ->
            state.update { it.copy(carreras = carreras, errorCareers = null) }
        }.onFailure {
            state.update { it.copy(errorCareers = "Error al cargar carreras") }
        }
    }

    private fun handleAlumnoUpdate(effect: ProfileEffect.SaveAlumnoUpdate) = launchInScope {
        runCatching {
            alumnoRepository.updateAlumno(effect.update.id, effect.update)
        }.onSuccess { result ->
            val carrera = state.value.carreras.find { it.id == result.carreraId }
            state.update {
                it.copy(
                    isSaving = false,
                    isEditing = false,
                    profileData = result,
                    selectedCareer = carrera,
                    errorMessage = null
                )
            }
            effectFlow.emit(ProfileEffect.ShowSnackbar(ProfileUiMessageEvent.UpdateSuccess))
        }.onFailure {
            state.update { it.copy(isSaving = false, errorMessage = "Error al guardar cambios") }
            effectFlow.emit(ProfileEffect.ShowSnackbar(ProfileUiMessageEvent.UpdateFailed))
        }
    }

    private fun handleLogout() = launchInScope {
        userPreferences.removeUserId()
        state.update { it.copy(navigateToLogin = true) }
    }
    private fun launchInScope(block: suspend () -> Unit) {
        scope.launch { block() }
    }
}

