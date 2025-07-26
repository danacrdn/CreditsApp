package com.example.creditsapp.data.repository

import com.example.creditsapp.data.network.BackendApiService
import com.example.creditsapp.domain.model.AlumnoActividad
import com.example.creditsapp.domain.model.CursoAlumno
import com.example.creditsapp.domain.model.Inscripcion
import retrofit2.Response

interface AlumnoActividadRepository {
    suspend fun getAlumnoActividadById(alumnoId: Int, actividadId: Int): AlumnoActividad
    suspend fun getActividadesPorAlumno(alumnoId: Int, estado: Int? = null): List<CursoAlumno>
    suspend fun createAlumnoActividad(inscripcion: Inscripcion): AlumnoActividad
    suspend fun updateAlumnoActividad(
        alumnoId: Int,
        actividadId: Int,
        alumnoActividad: AlumnoActividad
    )

    suspend fun deleteActividadAlumno(alumnoId: Int, actividadId: Int) : Response<Unit>
}

class DefaultAlumnoActividadRepository(private val apiService: BackendApiService) :
    AlumnoActividadRepository {
    override suspend fun getAlumnoActividadById(alumnoId: Int, actividadId: Int): AlumnoActividad =
        apiService.getAlumnoActividadById(alumnoId, actividadId)

    override suspend fun getActividadesPorAlumno(alumnoId: Int, estado: Int?) : List<CursoAlumno> =
        apiService.getActividadesPorAlumno(alumnoId, estado)

    override suspend fun createAlumnoActividad(inscripcion: Inscripcion): AlumnoActividad =
        apiService.createAlumnoActividad(inscripcion)

    override suspend fun updateAlumnoActividad(
        alumnoId: Int,
        actividadId: Int,
        alumnoActividad: AlumnoActividad
    ) = apiService.updateAlumnoActividad(alumnoId, actividadId, alumnoActividad)

    override suspend fun deleteActividadAlumno(alumnoId: Int, actividadId: Int) : Response<Unit> =
        apiService.deleteActividadAlumno(alumnoId, actividadId)

}