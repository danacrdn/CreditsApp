package com.example.creditsapp.ui.navigation

sealed class Screen(val name: String){
    object Login : Screen("login")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Activities : Screen("activities")
    object CompletedActivities : Screen("completed_activities")
    object ActivityDetails : Screen("activity_details")
    object Downloads : Screen("downloads")
    object TotalCredits : Screen("total_credits")
    object Suggestions : Screen("suggestions")
}