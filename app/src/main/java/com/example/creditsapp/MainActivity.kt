package com.example.creditsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.creditsapp.ui.navigation.CreditsAppNavigation
import com.example.creditsapp.ui.screens.LoginScreen
import com.example.creditsapp.ui.theme.CreditsAppTheme
import com.example.creditsapp.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreditsAppTheme {
                LoginScreen(modifier = Modifier.fillMaxSize(), LoginViewModel())
            }
        }
    }
}