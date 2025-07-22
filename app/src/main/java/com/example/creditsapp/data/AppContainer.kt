package com.example.creditsapp.data

import android.content.Context
import com.example.creditsapp.data.database.CreditsAppDatabase
import com.example.creditsapp.data.repository.DefaultPostsRepository
import com.example.creditsapp.data.repository.PostsRepository
import com.example.creditsapp.data.network.ApiService
import com.example.creditsapp.data.network.BackendApiService
import com.example.creditsapp.data.repository.ActividadesRepository
import com.example.creditsapp.data.repository.ActivitiesRepository
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.data.repository.AlumnosRepository
import com.example.creditsapp.data.repository.AuthRepository
import com.example.creditsapp.data.repository.CarreraRepository
import com.example.creditsapp.data.repository.DefaultActividadesRepository
import com.example.creditsapp.data.repository.DefaultActivitiesRepository
import com.example.creditsapp.data.repository.DefaultAlumnoActividadRepository
import com.example.creditsapp.data.repository.DefaultAlumnosRepository
import com.example.creditsapp.data.repository.DefaultAuthRepository
import com.example.creditsapp.data.repository.DefaultCarreraRepository
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
    val alumnosRepository: AlumnosRepository
    val actividadesRepository: ActividadesRepository
    val alumnoActividadRepository: AlumnoActividadRepository
    val authRepository: AuthRepository
    val carreraRepository: CarreraRepository
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

    // API de backend
    private val NEW_BASE_URL = "http://10.0.2.2:5091/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val backendRetrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(NEW_BASE_URL)
        .build()

    private val backendRetrofitService: BackendApiService by lazy {
        backendRetrofit.create(BackendApiService::class.java)
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

    override val alumnosRepository: AlumnosRepository by lazy {
        DefaultAlumnosRepository(backendRetrofitService)
    }

    override val actividadesRepository: ActividadesRepository by lazy {
        DefaultActividadesRepository(backendRetrofitService)
    }

    override val alumnoActividadRepository: AlumnoActividadRepository by lazy {
        DefaultAlumnoActividadRepository(backendRetrofitService)
    }

    override val authRepository: AuthRepository by lazy {
        DefaultAuthRepository(backendRetrofitService)
    }

    override val carreraRepository: CarreraRepository by lazy {
        DefaultCarreraRepository(backendRetrofitService)
    }
}