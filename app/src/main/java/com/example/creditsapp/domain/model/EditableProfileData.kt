package com.example.creditsapp.domain.model


data class EditableProfileData(
    val id: Int?= null,
    val nombre: String = "",
    val apellido: String = "",
    val semestre: Int? = null,
    val totalCreditos: Double = 0.0,
    val carrera: Carrera? = null,
    val correoElectronico: String = "",
    val currentPassword: String = "",
    val newPassword: String = ""
)
