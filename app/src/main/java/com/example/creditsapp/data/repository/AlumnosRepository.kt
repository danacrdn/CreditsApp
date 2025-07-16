package com.example.creditsapp.data.repository

import android.util.Log
import com.example.creditsapp.data.network.BackendApiService
import com.example.creditsapp.domain.model.Alumno

interface AlumnosRepository {
    suspend fun getAlumnos(): List<Alumno>
    suspend fun getAlumnoById(id: Int): Alumno
    suspend fun updateAlumno(): Alumno
}

class DefaultAlumnosRepository(private val apiService: BackendApiService) : AlumnosRepository {
    override suspend fun getAlumnos(): List<Alumno> = apiService.getAlumnos()

    override suspend fun getAlumnoById(id: Int): Alumno = apiService.getAlumnoById(id)

    override suspend fun updateAlumno(): Alumno = apiService.updateAlumno()
}