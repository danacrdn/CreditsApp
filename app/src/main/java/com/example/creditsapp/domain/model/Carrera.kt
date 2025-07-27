package com.example.creditsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Carrera(
    val id: Int,
    val nombre: String
)