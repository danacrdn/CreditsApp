package com.example.creditsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.creditsapp.R
import com.example.creditsapp.ui.components.TopBar
import com.example.creditsapp.ui.navigation.Screen

@Composable
fun SuggestionsScreen(
    navController: NavController
){
    Scaffold(
        topBar = { TopBar(
            R.string.questions_and_suggestions, navigateBack = { navController.popBackStack() },
            navigateToProfile = { navController.navigate(Screen.Profile.name)}) },
        content = { paddingValues ->
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ){

            }
        }
    )
}