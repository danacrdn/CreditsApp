package com.example.creditsapp.data

import android.content.Context
import com.example.creditsapp.data.database.CreditsAppDatabase
import com.example.creditsapp.data.repository.DefaultPostsRepository
import com.example.creditsapp.data.repository.PostsRepository
import com.example.creditsapp.data.network.ApiService
import com.example.creditsapp.data.repository.ActivitiesRepository
import com.example.creditsapp.data.repository.DefaultActivitiesRepository
import com.example.creditsapp.data.repository.DefaultUserActivitiesRepository
import com.example.creditsapp.data.repository.DefaultUsersRepository
import com.example.creditsapp.data.repository.UserActivitiesRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.repository.UsersRepository
import com.example.creditsapp.data.repository.dataStore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val userPreferences: UserPreferencesRepository
    val postsRepository: PostsRepository
    val activitiesRepository: ActivitiesRepository
    val usersRepository: UsersRepository
    val userActivitiesRepository: UserActivitiesRepository
}

class DefaultAppContainer (context: Context): AppContainer {
    private val dataStore = context.dataStore

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override val userPreferences = UserPreferencesRepository(dataStore, appScope)

    private val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val postsRepository: PostsRepository by lazy {
        DefaultPostsRepository(retrofitService)
    }

    override val activitiesRepository: ActivitiesRepository by lazy {
        DefaultActivitiesRepository(CreditsAppDatabase.getDatabase(context).activityDao())
    }

    override val usersRepository: UsersRepository by lazy {
        DefaultUsersRepository(CreditsAppDatabase.getDatabase(context).userDao())
    }

    override val userActivitiesRepository: UserActivitiesRepository by lazy {
        DefaultUserActivitiesRepository(CreditsAppDatabase.getDatabase(context).userActivityDao())
    }

}