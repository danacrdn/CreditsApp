package com.example.creditsapp.data.repository

import com.example.creditsapp.data.network.BackendApiService
import com.example.creditsapp.domain.model.LoginRequest
import com.example.creditsapp.domain.model.LoginResponse
import com.example.creditsapp.domain.model.RegisterRequest

interface AuthRepository {
    suspend fun register(registerRequest: RegisterRequest): String
    suspend fun login(loginRequest: LoginRequest): LoginResponse
}

class DefaultAuthRepository(private val apiService: BackendApiService) : AuthRepository {
    override suspend fun register(registerRequest: RegisterRequest): String =
        apiService.register(registerRequest)

    override suspend fun login(loginRequest: LoginRequest): LoginResponse =
        apiService.login(loginRequest)

}