package com.example.creditsapp.presentation.viewmodel.details

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.creditsapp.data.repository.ActividadesRepository
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.domain.model.AlumnoActividad
import com.example.creditsapp.domain.model.Inscripcion
import com.example.creditsapp.presentation.utilities.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate

class ActivityDetailsEffectHandler(
    private val alumnoActividadRepository: AlumnoActividadRepository,
    private val actividadRepository: ActividadesRepository,
    private val userPreferences: UserPreferencesRepository,
    private val viewModelScope: CoroutineScope,
    private val effect: MutableSharedFlow<ActivityDetailsEffect>,
    private val activityId: Int
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun handle(intent: ActivityDetailsIntent) {
        when (intent) {
            is ActivityDetailsIntent.LoadActivityDetails -> {
                viewModelScope.launch {
                    userPreferences.userId.filterNotNull().collect { alumnoId ->
                        val result = runCatching {
                            val actividad = actividadRepository.getActividadById(activityId)
                            val estado = alumnoActividadRepository.getAlumnoActividadByIdOrNull(
                                alumnoId,
                                activityId
                            )
                                .toAlumnoActividadState()
                            ActividadUiData(actividad, estado)
                        }
                        effect.emit(ActivityDetailsEffect.DataLoaded(result))
                    }
                }
            }

            is ActivityDetailsIntent.PerformAction -> {
                viewModelScope.launch {
                    userPreferences.userId.filterNotNull().collect { id ->
                        val message = when (intent.action) {
                            is AlumnoActividadAction.InscribirAlumno -> inscribir(id)
                            is AlumnoActividadAction.EliminarActividad -> eliminar(id)
                        }
                        effect.emit(ActivityDetailsEffect.ShowSnackbar(message))
                    }
                }
            }

            is ActivityDetailsIntent.DataLoaded -> {}
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun inscribir(id: Int): AlumnoActividadUiMessageEvent =
        runUiMessageAction(
            block = {
                alumnoActividadRepository.createAlumnoActividad(
                    Inscripcion(
                        actividadId = activityId,
                        alumnoId = id,
                        estadoAlumnoActividad = 1,
                        fechaInscripcion = LocalDate.now().toString(),
                        genero = 0
                    )
                )
            },
            onSuccess = AlumnoActividadUiMessageEvent.InscriptionSuccess,
            onFailure = AlumnoActividadUiMessageEvent.InscriptionFailed
        )

    private suspend fun eliminar(id: Int): AlumnoActividadUiMessageEvent =
        runUiMessageAction(
            block = {
                alumnoActividadRepository.deleteActividadAlumno(id, activityId)
            },
            onSuccess = AlumnoActividadUiMessageEvent.DeleteSuccess,
            onFailure = AlumnoActividadUiMessageEvent.DeleteFailed
        )

    // función pura
    private fun AlumnoActividad?.toAlumnoActividadState(): AlumnoActividadState =
        when (this?.estadoAlumnoActividad) {
            1 -> AlumnoActividadState.Inscrito
            2 -> AlumnoActividadState.EnCurso
            3 -> AlumnoActividadState.Completado
            4 -> AlumnoActividadState.Acreditado
            5 -> AlumnoActividadState.NoAcreditado
            null -> AlumnoActividadState.NoInscrito
            else -> AlumnoActividadState.Desconocido
        }
    // función pura
    private suspend fun <T> runUiMessageAction(
        block: suspend () -> T,
        onSuccess: AlumnoActividadUiMessageEvent,
        onFailure: AlumnoActividadUiMessageEvent
    ): AlumnoActividadUiMessageEvent =
        runCatching { block() }.fold(
            onSuccess = { onSuccess },
            onFailure = { onFailure }
        )

    private suspend fun AlumnoActividadRepository.getAlumnoActividadByIdOrNull(alumnoId: Int, actividadId: Int) =
        runCatching { getAlumnoActividadById(alumnoId, actividadId) }.getOrNull()
}

