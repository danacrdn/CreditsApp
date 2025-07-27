package com.example.creditsapp.data.repository

import com.example.creditsapp.data.network.BackendApiService
import com.example.creditsapp.domain.model.Aviso
import com.example.creditsapp.domain.model.Carrera

interface AvisoRepository {
    suspend fun getAvisos(): List<Aviso>
}

class DefaultAvisoRepository(private val apiService: BackendApiService) : AvisoRepository {
    override suspend fun getAvisos(): List<Aviso> = apiService.getAvisos()
}