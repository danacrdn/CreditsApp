package com.example.creditsapp.presentation.viewmodel.details

import com.example.creditsapp.domain.model.Actividad
import com.example.creditsapp.presentation.utilities.UiState

data class ActividadUiData(
    val actividad: Actividad? = null,
    val alumnoActividadState: AlumnoActividadState
)

data class ActivityDetailsUiState(
    val dataState: UiState<ActividadUiData> = UiState.Loading,
    val isPerformingAction: Boolean = false,
    val errorMessage: String? = null
)

sealed class ActivityDetailsIntent {
    object LoadActivityDetails : ActivityDetailsIntent()
    data class PerformAction(val action: AlumnoActividadAction) : ActivityDetailsIntent()
}

sealed class ActivityDetailsEffect {
    data class ShowSnackbar(val message: AlumnoActividadUiMessageEvent) : ActivityDetailsEffect()
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

sealed class AlumnoActividadAction {
    object InscribirAlumno : AlumnoActividadAction()
    object  EliminarActividad : AlumnoActividadAction()
}