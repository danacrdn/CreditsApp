package com.example.creditsapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.creditsapp.ui.screens.ActivityDetailsScreen
import com.example.creditsapp.ui.screens.CompletedActivitiesScreen
import com.example.creditsapp.ui.screens.ConsultCreditsScreen
import com.example.creditsapp.ui.screens.DownloadDocumentsScreen
import com.example.creditsapp.ui.screens.HomeScreen
import com.example.creditsapp.ui.screens.LoginScreen
import com.example.creditsapp.ui.screens.ProfileScreen
import com.example.creditsapp.ui.screens.SuggestionsScreen
import com.example.creditsapp.viewmodel.LoginViewModel

@Composable
fun CreditsAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.name
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

        composable(Screen.Suggestions.name) {
            SuggestionsScreen(navController)
        }

    }
}


