package com.example.creditsapp.data.repository

import com.example.creditsapp.data.network.BackendApiService
import com.example.creditsapp.domain.model.LoginRequest
import com.example.creditsapp.domain.model.LoginResponse
import com.example.creditsapp.domain.model.RegisterRequest
import com.example.creditsapp.domain.model.RegisterResponse

interface AuthRepository {
    suspend fun register(registerRequest: RegisterRequest): RegisterResponse
    suspend fun login(loginRequest: LoginRequest): LoginResponse
}

class DefaultAuthRepository(private val apiService: BackendApiService) : AuthRepository {
    override suspend fun register(registerRequest: RegisterRequest): RegisterResponse =
        apiService.register(registerRequest)

    override suspend fun login(loginRequest: LoginRequest): LoginResponse =
        apiService.login(loginRequest)

}