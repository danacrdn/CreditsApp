package com.example.creditsapp.data.repository

import com.example.creditsapp.data.network.BackendApiService
import com.example.creditsapp.domain.model.Carrera

interface CarreraRepository {
    suspend fun getCarreras(): List<Carrera>
}

class DefaultCarreraRepository(private val apiService: BackendApiService) : CarreraRepository {
    override suspend fun getCarreras(): List<Carrera> = apiService.getCarreras()
}