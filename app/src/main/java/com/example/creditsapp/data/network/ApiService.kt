package com.example.creditsapp.data.network

import com.example.creditsapp.domain.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}