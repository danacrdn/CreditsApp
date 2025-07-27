package com.example.creditsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Alumno (
    val id: Int,
    val numeroControl: String,
    val nombre: String,
    val apellido: String,
    val correoElectronico: String,
    val fechaRegistro: String,
    val semestre: Int,
    val totalCreditos: Double,
    val carreraNombre: String? = null,
    val carreraId: Int
)

@Serializable
data class AlumnoUpdate (
    val id: Int,
    val nombre: String,
    val apellido: String,
    val semestre: Int,
    val totalCreditos: Double,
    val carreraId: Int,
    val correoElectronico: String,
    val currentPassword: String,
    val newPassword: String
)
