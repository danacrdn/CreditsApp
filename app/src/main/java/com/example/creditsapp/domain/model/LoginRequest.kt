package com.example.creditsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val usuario: String,
    val password: String
)

@Serializable
data class LoginResponse (
    val token: String,
    val expiration: String,
    val alumnoId: Int
)