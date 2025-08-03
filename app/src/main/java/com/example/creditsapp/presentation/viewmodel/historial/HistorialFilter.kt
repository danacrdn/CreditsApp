package com.example.creditsapp.presentation.viewmodel.historial

import com.example.creditsapp.domain.model.CursoAlumno

fun filtrarActividades(actividades: List<CursoAlumno>): List<CursoAlumno> =
    actividades.filter(ReglasActividad.debeEstarCompletada)

typealias FiltroActividad = (CursoAlumno) -> Boolean

object ReglasActividad {
    val debeEstarCompletada: FiltroActividad = { it.estadoAlumnoActividad == 4 }
}