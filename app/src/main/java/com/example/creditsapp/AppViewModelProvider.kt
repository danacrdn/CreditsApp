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


object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {

        initializer {
            ActivitiesViewModel(activitiesRepository = creditsApp().container.activitiesRepository)
        }

        initializer {
            UserActivitiesViewModel(userActivitiesRepository = creditsApp().container.userActivitiesRepository)
        }

        initializer {
            ActivityDetailsViewModel(
                this.createSavedStateHandle(),
                activitiesRepository = creditsApp().container.activitiesRepository,
                userActivitiesRepository = creditsApp().container.userActivitiesRepository
            )
        }

        initializer {
            ConsultCreditsViewModel(userActivitiesRepository = creditsApp().container.userActivitiesRepository)
        }

        initializer {
            ProfileViewModel(usersRepository = creditsApp().container.usersRepository)
        }

        initializer {
            HomeViewModel(userActivitiesRepository = creditsApp().container.userActivitiesRepository)
        }
    }
}

fun CreationExtras.creditsApp(): CreditsApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)