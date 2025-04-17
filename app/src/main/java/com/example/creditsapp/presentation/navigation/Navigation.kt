package com.example.creditsapp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.creditsapp.presentation.screens.ActivitiesScreen
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
        startDestination = Screen.Login.name
    ) {

        composable(Screen.Login.name) {
            LoginScreen(
                modifier = Modifier.fillMaxSize(),
                loginViewModel = LoginViewModel(),
                navController = navController
            )

        }

        composable(Screen.Home.name) {
            HomeScreen(navController)
        }

        composable(Screen.Profile.name) {
            ProfileScreen(navController)
        }

        composable(Screen.Activities.name) {
            ActivitiesScreen(navController)
        }

        composable(Screen.CompletedActivities.name) {
            CompletedActivitiesScreen(navController)
        }

        composable(
            "activity_details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            ActivityDetailsScreen(activityId = id, navController)
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


