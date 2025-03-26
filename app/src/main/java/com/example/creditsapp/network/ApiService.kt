package com.example.creditsapp.network

import com.example.creditsapp.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}