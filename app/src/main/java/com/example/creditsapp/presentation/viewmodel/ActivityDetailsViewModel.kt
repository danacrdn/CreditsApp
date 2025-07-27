package com.example.creditsapp.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.repository.ActividadesRepository
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.domain.model.Actividad
import com.example.creditsapp.domain.model.Inscripcion
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Instant

class ActivityDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val alumnoActividadRepository: AlumnoActividadRepository,
    private val actividadRepository: ActividadesRepository,
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ActividadUiState>(ActividadUiState.Loading)
    val uiState: StateFlow<ActividadUiState> = _uiState

    private val _snackbarMessage = MutableSharedFlow<AlumnoActividadUiMessageEvent>()
    val snackbarMessage: SharedFlow<AlumnoActividadUiMessageEvent> = _snackbarMessage

    private val activityId: Int = checkNotNull(savedStateHandle["id"]) { "ID not found" }

    private suspend fun obtenerAlumnoId(): Int? {
        val alumnoId = userPreferences.userId.first()
        return alumnoId
    }

    init {
        fetchActividad()
    }

    private fun fetchActividad() {
        viewModelScope.launch {
            try {
                val actividad = actividadRepository.getActividadById(activityId)

                val alumnoId = obtenerAlumnoId()
                val alumnoActividadState = alumnoId?.let { getAlumnoActividadState(activityId, it) }
                    ?: AlumnoActividadState.Desconocido

                _uiState.value = ActividadUiState.Success(
                    actividad = actividad,
                    alumnoActividadState = alumnoActividadState
                )

            } catch (e: IOException) {
                _uiState.value = ActividadUiState.Error
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun inscribirAlumno() {
        viewModelScope.launch {
            val alumnoId = obtenerAlumnoId()
            if (alumnoId == null) {
                _snackbarMessage.emit(AlumnoActividadUiMessageEvent.InscriptionFailed)
                return@launch
            }

            try {
                val nuevaInscripcion = Inscripcion(
                    alumnoId = alumnoId,
                    actividadId = activityId,
                    estadoAlumnoActividad = 1,
                    fechaInscripcion = Instant.now().toString(),
                    genero = 1
                )
                println(nuevaInscripcion)
                val response = alumnoActividadRepository.createAlumnoActividad(nuevaInscripcion)
                println("Se ha inscrito correctamente $response")
                fetchActividad()
                _snackbarMessage.emit(AlumnoActividadUiMessageEvent.InscriptionSuccess)
            } catch (e: Exception) {
                println("Fallo al inscribir $e")
                _snackbarMessage.emit(AlumnoActividadUiMessageEvent.InscriptionFailed)
            }
        }
    }


    fun eliminarActividad() {
        viewModelScope.launch {
            val alumnoId = obtenerAlumnoId()
            if (alumnoId == null) {
                _snackbarMessage.emit(AlumnoActividadUiMessageEvent.DeleteFailed)
                return@launch
            }
            try {
                alumnoActividadRepository.deleteActividadAlumno(alumnoId, activityId)
                fetchActividad()
                _snackbarMessage.emit(AlumnoActividadUiMessageEvent.DeleteSuccess)

            } catch (e: Exception) {
                println("No se pudo eliminar la actividad: $e")
                _snackbarMessage.emit(AlumnoActividadUiMessageEvent.DeleteFailed)
            }
        }
    }

    private suspend fun getAlumnoActividadState(
        activityId: Int,
        alumnoId: Int
    ): AlumnoActividadState {
        return try {
            val alumnoAct = alumnoActividadRepository.getAlumnoActividadById(alumnoId, activityId)
            when (alumnoAct.estadoAlumnoActividad) {
                1 -> AlumnoActividadState.Inscrito
                2 -> AlumnoActividadState.EnCurso
                3 -> AlumnoActividadState.Completado
                4 -> AlumnoActividadState.Acreditado
                5 -> AlumnoActividadState.NoAcreditado
                else -> AlumnoActividadState.Desconocido
            }
        } catch (e: Exception) {
            AlumnoActividadState.NoInscrito
        }
    }
}

sealed interface ActividadUiState {
    data class Success(
        val actividad: Actividad? = null,
        val alumnoActividadState: AlumnoActividadState
    ) : ActividadUiState

    object Error : ActividadUiState
    object Loading : ActividadUiState
}

sealed class AlumnoActividadState {
    object Inscrito : AlumnoActividadState()
    object EnCurso : AlumnoActividadState()
    object Completado : AlumnoActividadState()
    object Acreditado : AlumnoActividadState()
    object NoAcreditado : AlumnoActividadState()
    object Desconocido : AlumnoActividadState()
    object NoInscrito : AlumnoActividadState()
}

sealed class AlumnoActividadUiMessageEvent {
    object InscriptionSuccess : AlumnoActividadUiMessageEvent()
    object InscriptionFailed : AlumnoActividadUiMessageEvent()
    object DeleteSuccess : AlumnoActividadUiMessageEvent()
    object DeleteFailed : AlumnoActividadUiMessageEvent()
}
