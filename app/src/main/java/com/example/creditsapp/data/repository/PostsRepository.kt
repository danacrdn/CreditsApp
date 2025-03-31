package com.example.creditsapp.data.repository

import com.example.creditsapp.domain.model.Post
import com.example.creditsapp.data.network.ApiService

interface PostsRepository {
    suspend fun getPosts(): List<Post>
}

class DefaultPostsRepository(
    private val apiService: ApiService
) : PostsRepository {
    override suspend fun getPosts(): List<Post> = apiService.getPosts()
}