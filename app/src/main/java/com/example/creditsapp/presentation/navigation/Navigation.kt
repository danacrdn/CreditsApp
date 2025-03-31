package com.example.creditsapp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.creditsapp.presentation.screens.ActivityDetailsScreen
import com.example.creditsapp.presentation.screens.CompletedActivitiesScreen
import com.example.creditsapp.presentation.screens.ConsultCreditsScreen
import com.example.creditsapp.presentation.screens.DownloadDocumentsScreen
import com.example.creditsapp.presentation.screens.HomeScreen
import com.example.creditsapp.presentation.screens.LoginScreen
import com.example.creditsapp.presentation.screens.ProfileScreen
import com.example.creditsapp.presentation.screens.PostsScreen
import com.example.creditsapp.presentation.viewmodel.PostsViewModel
import com.example.creditsapp.presentation.viewmodel.LoginViewModel

@Composable
fun CreditsAppNavigation() {
    val navController = rememberNavController()
    val postsViewModel: PostsViewModel =
        viewModel(factory = PostsViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.name
    ) {

        composable(Screen.Login.name) {
            LoginScreen(modifier = Modifier.fillMaxSize(), loginViewModel = LoginViewModel(), navController = navController)

        }

        composable(Screen.Home.name) {
            HomeScreen(navController)
        }

        composable(Screen.Profile.name) {
            ProfileScreen(navController)
        }

        // Pantalla aún no implementada
        composable(Screen.Activities.name) {
        }

        composable(Screen.CompletedActivities.name) {
            CompletedActivitiesScreen(navController)
        }

        composable(Screen.ActivityDetails.name) {
            ActivityDetailsScreen(activityId = 1, navController)
        }

        composable(Screen.Downloads.name) {
            DownloadDocumentsScreen(navController)
        }

        composable(Screen.TotalCredits.name) {
            ConsultCreditsScreen(navController)
        }

        composable(Screen.Posts.name) {
            PostsScreen(
                navController,
                postsUiState = postsViewModel.postsUiState
            )
        }

    }
}


