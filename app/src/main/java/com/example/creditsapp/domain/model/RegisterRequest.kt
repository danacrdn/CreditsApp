package com.example.creditsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val email: String,
    val password: String,
    val confirmPassword: String,
    val numeroControl: String,
    val nombre: String,
    val apellido: String,
    val semestre: Int,
    val totalCreditos: Double,
    val carreraId: Int
)