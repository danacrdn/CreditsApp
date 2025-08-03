package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.presentation.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel(private val userPreferences: UserPreferencesRepository) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents

    val userId: StateFlow<Int?> = userPreferences.userId

    fun handleSplashNavigation(delayMs: Long = 1000) {
        viewModelScope.launch {
            delay(delayMs)
            userId.value
                .let(::determineSplashNavigationRoute)
                .let { route ->
                    _navigationEvents.emit(
                        NavigationEvent.NavigateTo(
                            route = route,
                            popUpTo = Screen.Splash.name,
                            inclusive = true
                        )
                    )
                }
        }
    }
    val isDarkMode: StateFlow<Boolean> = userPreferences.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun switchDarkMode(): Unit = viewModelScope.launch {
        isDarkMode.value
            .let(::toggleDarkMode)
            .let { newValue -> userPreferences.saveDarkMode(newValue) }
    }.let { }
}

fun toggleDarkMode(current: Boolean): Boolean = !current

sealed class NavigationEvent {
    data class NavigateTo(val route: String, val popUpTo: String, val inclusive: Boolean = true) : NavigationEvent()
}

fun determineSplashNavigationRoute(userId: Int?): String =
    userId?.let { Screen.Home.name } ?: Screen.Login.name