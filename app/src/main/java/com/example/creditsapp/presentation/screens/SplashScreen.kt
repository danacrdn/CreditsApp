package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.presentation.viewmodel.NavigationEvent
import com.example.creditsapp.presentation.viewmodel.SessionViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    LaunchedEffect(Unit) {
        sessionViewModel.handleSplashNavigation()
    }

    LaunchedEffect(Unit) {
        sessionViewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route) {
                        popUpTo(event.popUpTo) { inclusive = event.inclusive }
                    }
                }
            }
        }
    }

    Surface {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}