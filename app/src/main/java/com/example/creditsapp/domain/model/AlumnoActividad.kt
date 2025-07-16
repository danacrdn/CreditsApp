package com.example.creditsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AlumnoActividad (
    val alumnoId: Int,
    val alumnoNombre: String,
    val actividadId: Int,
    val estadoAlumnoActividad: Int,
    val fechaRegistro: String,
    val creditosObtenidos: Double
)

@Serializable
data class Inscripcion (
    val alumnoId: Int,
    val actividadId: Int,
    val estadoAlumnoActividad: Int,
    val fechaInscripcion: String
)

@Serializable
data class UpdateInscripcion (
    val alumnoId: Int,
    val actividadId: Int,
    val estadoAlumnoActividad: Int,
    val fechaInscripcion: String
)

// cursos obtenidos por alumno
@Serializable
data class CursoAlumno(
    val actividadId: Int,
    val nombre: String,
    val descripcion: String,
    val imagenNombre: String,
    val creditos: Double,
    val fechaInicio: String,
    val fechaFin: String,
    val estadoAlumnoActividad: Int
)