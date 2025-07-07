package com.example.creditsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.creditsapp.presentation.navigation.CreditsAppNavigation
import com.example.creditsapp.presentation.viewmodel.SessionViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sessionViewModel: SessionViewModel =
                viewModel(factory = AppViewModelProvider.Factory)

            val isDarkMode by sessionViewModel.isDarkMode.collectAsState()

            CreditsAppTheme (darkTheme = isDarkMode){
                CreditsAppNavigation()
            }
        }
    }
}