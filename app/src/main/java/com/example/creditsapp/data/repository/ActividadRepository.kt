package com.example.creditsapp.data.repository

import com.example.creditsapp.data.network.BackendApiService
import com.example.creditsapp.domain.model.Actividad

interface ActividadesRepository {
    suspend fun getActividades(): List<Actividad>
    suspend fun getActividadById(id: Int): Actividad
}

class DefaultActividadesRepository(private val apiService: BackendApiService) : ActividadesRepository {
    override suspend fun getActividades(): List<Actividad> {
        return apiService.getActividades()
    }

    override suspend fun getActividadById(id: Int): Actividad = apiService.getActividadById(id)
}