package com.example.creditsapp.data.network

import com.example.creditsapp.domain.model.Actividad
import com.example.creditsapp.domain.model.Alumno
import com.example.creditsapp.domain.model.AlumnoActividad
import com.example.creditsapp.domain.model.Carrera
import com.example.creditsapp.domain.model.Inscripcion
import com.example.creditsapp.domain.model.LoginRequest
import com.example.creditsapp.domain.model.LoginResponse
import com.example.creditsapp.domain.model.RegisterRequest
import com.example.creditsapp.domain.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BackendApiService {

    // Alumno
    @GET("/api/Alumno")
    suspend fun getAlumnos(): List<Alumno>

    @GET("/api/Alumno/{id}")
    suspend fun getAlumnoById(@Path("id") id: Int): Alumno

    @PUT("/api/Alumno")
    suspend fun updateAlumno(): Alumno

    // Actividad
    @GET("/api/Actividades")
    suspend fun getActividades(): List<Actividad>

    @GET("/api/Actividades/{id}")
    suspend fun getActividadById(@Path("id") id: Int): Actividad

    // AlumnoActividad
    @GET("/api/AlumnoActividad/{alumnoId}/{actividadId}")
    suspend fun getAlumnoActividadById(
        @Path("alumnoId") alumnoId: Int,
        @Path("actividadId") actividadId: Int
    ): AlumnoActividad

    @GET("/api/AlumnoActividad/cursos-alumno/{alumnoId}")
    suspend fun getActividadesPorAlumno(
        @Path("alumnoId") alumnoId: Int,
        @Query("estado") estado: Int? = null
    ): List<AlumnoActividad>

    @POST("/api/AlumnoActividad")
    suspend fun createAlumnoActividad(
        @Body inscripcion: Inscripcion
    ): AlumnoActividad

    @PUT("/api/AlumnoActividad/{alumnoId}/{actividadId}")
    suspend fun updateAlumnoActividad(
        @Path("alumnoId") alumnoId: Int,
        @Path("actividadId") actividadId: Int,
        @Body alumnoActividad: AlumnoActividad
    )

    @DELETE("/api/AlumnoActividad/{alumnoId}/{actividadId}")
    suspend fun deleteActividadAlumno(
        @Path("alumnoId") alumnoId: Int,
        @Path("actividadId") actividadId: Int,
    )

    // Carrera
    @GET("/api/Carrera/carreras")
    suspend fun getCarreras(): List<Carrera>

    // Auth
    @POST("/api/Auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("api/Auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}