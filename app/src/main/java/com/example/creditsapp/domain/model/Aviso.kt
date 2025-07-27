package com.example.creditsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Aviso (
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val departamentoId: Int?,
    val departamentoNombre: String?,
    val coordinadorId: Int?,
    val coordinadorNombre: String?,
    val coordinadorApellido: String?,
)

