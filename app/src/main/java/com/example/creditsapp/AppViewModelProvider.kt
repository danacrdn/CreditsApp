package com.example.creditsapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.creditsapp.presentation.viewmodel.historial.ActivitiesHistorialViewModel
import com.example.creditsapp.presentation.viewmodel.activities.ActivitiesViewModel
import com.example.creditsapp.presentation.viewmodel.details.ActivityDetailsViewModel
import com.example.creditsapp.presentation.viewmodel.announcements.AnnouncementsViewModel
import com.example.creditsapp.presentation.viewmodel.home.HomeViewModel
import com.example.creditsapp.presentation.viewmodel.login.LoginViewModel
import com.example.creditsapp.presentation.viewmodel.profile.ProfileViewModel
import com.example.creditsapp.presentation.viewmodel.register.RegisterViewModel
import com.example.creditsapp.presentation.viewmodel.SessionViewModel
import com.example.creditsapp.presentation.viewmodel.userActivities.UserActivitiesViewModel


object AppViewModelProvider {
    @RequiresApi(Build.VERSION_CODES.O)
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
                actividadesRepository = creditsApp().container.actividadesRepository
            )
        }

        initializer {
            UserActivitiesViewModel (
                alumnoActividadRepository = creditsApp().container.alumnoActividadRepository,
                userPreferences = creditsApp().container.userPreferences
            )
        }

        initializer {
            ActivityDetailsViewModel(
                this.createSavedStateHandle(),
                actividadRepository = creditsApp().container.actividadesRepository,
                userPreferences = creditsApp().container.userPreferences,
                alumnoActividadRepository = creditsApp().container.alumnoActividadRepository
            )
        }

        initializer {
            ActivitiesHistorialViewModel(
                alumnoActividadRepository = creditsApp().container.alumnoActividadRepository,
                userPreferences = creditsApp().container.userPreferences
            )
        }

        initializer {
            ProfileViewModel(
                alumnoRepository = creditsApp().container.alumnosRepository,
                userPreferences = creditsApp().container.userPreferences,
                carreraRepository = creditsApp().container.carreraRepository
            )
        }

        initializer {
            LoginViewModel(
                userPreferences = creditsApp().container.userPreferences,
                authRepository = creditsApp().container.authRepository
            )
        }

        initializer {
            RegisterViewModel(
                carreraRepository = creditsApp().container.carreraRepository,
                authRepository = creditsApp().container.authRepository
            )
        }

        initializer {
            AnnouncementsViewModel(
                avisoRepository = creditsApp().container.avisoRepository
            )
        }
    }
}

fun CreationExtras.creditsApp(): CreditsApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)