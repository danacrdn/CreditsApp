package com.example.creditsapp.presentation.navigation

sealed class Screen(val name: String){
    object Login : Screen("login")
    object Register : Screen("Register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Activities : Screen("activities")
    object CompletedActivities : Screen("completed_activities")
    object ActivityDetails : Screen("activity_details")
    object Downloads : Screen("downloads")
    object TotalCredits : Screen("total_credits")
    object Posts : Screen("posts")
    object Splash : Screen("splash")
}