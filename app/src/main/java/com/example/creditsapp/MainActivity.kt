package com.example.creditsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.creditsapp.presentation.navigation.CreditsAppNavigation
import com.example.creditsapp.ui.theme.CreditsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreditsAppTheme {
                CreditsAppNavigation()
            }
        }
    }
}