package com.example.creditsapp.data

import com.example.creditsapp.model.Post
import com.example.creditsapp.network.ApiService

interface PostsRepository {
    suspend fun getPosts(): List<Post>
}

class DefaultPostsRepository(
    private val apiService: ApiService
) : PostsRepository {
    override suspend fun getPosts(): List<Post> = apiService.getPosts()
}