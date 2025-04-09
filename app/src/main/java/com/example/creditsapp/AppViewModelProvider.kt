package com.example.creditsapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.creditsapp.presentation.viewmodel.ActivitiesViewModel
import com.example.creditsapp.presentation.viewmodel.ActivityDetailsViewModel
import com.example.creditsapp.presentation.viewmodel.UserActivitiesViewModel
import com.example.creditsapp.presentation.viewmodel.ConsultCreditsViewModel
import com.example.creditsapp.presentation.viewmodel.ProfileViewModel
import com.example.creditsapp.presentation.viewmodel.HomeViewModel


object AppViewModelProvider {
    val Factory:  ViewModelProvider.Factory = viewModelFactory {

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)
            val activitiesRepository = application.container.activitiesRepository
            ActivitiesViewModel(activitiesRepository = activitiesRepository)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)
            val userActivitiesRepository = application.container.userActivitiesRepository
            UserActivitiesViewModel(userActivitiesRepository = userActivitiesRepository)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)
            val activitiesRepository = application.container.activitiesRepository
            ActivityDetailsViewModel(this.createSavedStateHandle(), activitiesRepository = activitiesRepository)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)
            val userActivitiesRepository = application.container.userActivitiesRepository
            ConsultCreditsViewModel(userActivitiesRepository = userActivitiesRepository)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)
            val usersRepository = application.container.usersRepository
            ProfileViewModel(usersRepository = usersRepository)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)
            val userActivitiesRepository = application.container.userActivitiesRepository
            HomeViewModel(userActivitiesRepository = userActivitiesRepository)
        }
    }
}