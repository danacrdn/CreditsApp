package com.example.creditsapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.creditsapp.presentation.viewmodel.ActivitiesViewModel
import com.example.creditsapp.presentation.viewmodel.ActivityDetailsViewModel
import com.example.creditsapp.presentation.viewmodel.UserActivitiesViewModel
import com.example.creditsapp.presentation.viewmodel.ConsultCreditsViewModel
import com.example.creditsapp.presentation.viewmodel.ProfileViewModel
import com.example.creditsapp.presentation.viewmodel.HomeViewModel
import com.example.creditsapp.presentation.viewmodel.LoginViewModel
import com.example.creditsapp.presentation.viewmodel.PostsViewModel
import com.example.creditsapp.presentation.viewmodel.RegisterViewModel
import com.example.creditsapp.presentation.viewmodel.SessionViewModel


object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {

        initializer {
            SessionViewModel(creditsApp().container.userPreferences)
        }

        initializer {
            HomeViewModel(
                alumnosRepository = creditsApp().container.alumnosRepository,
                userPreferences = creditsApp().container.userPreferences,
            )
        }

        initializer {
            ActivitiesViewModel(
                activitiesRepository = creditsApp().container.activitiesRepository,
                actividadesRepository = creditsApp().container.actividadesRepository
            )
        }

        initializer {
            UserActivitiesViewModel(
                userActivitiesRepository = creditsApp().container.userActivitiesRepository,
                userPreferences = creditsApp().container.userPreferences
            )
        }

        initializer {
            ActivityDetailsViewModel(
                this.createSavedStateHandle(),
                activitiesRepository = creditsApp().container.activitiesRepository,
                userActivitiesRepository = creditsApp().container.userActivitiesRepository,
                userPreferences = creditsApp().container.userPreferences
            )
        }

        initializer {
            ConsultCreditsViewModel(
                userActivitiesRepository = creditsApp().container.userActivitiesRepository,
                userPreferences = creditsApp().container.userPreferences
            )
        }

        initializer {
            ProfileViewModel(
                alumnoRepository = creditsApp().container.alumnosRepository,
                userPreferences = creditsApp().container.userPreferences
            )
        }

        initializer {
            LoginViewModel(
                userPreferences = creditsApp().container.userPreferences,
                authRepository = creditsApp().container.authRepository
            )
        }

        initializer {
            PostsViewModel(
                postsRepository = creditsApp().container.postsRepository
            )
        }

        initializer {
            RegisterViewModel(
                carreraRepository = creditsApp().container.carreraRepository,
                authRepository = creditsApp().container.authRepository
            )
        }
    }
}

fun CreationExtras.creditsApp(): CreditsApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)