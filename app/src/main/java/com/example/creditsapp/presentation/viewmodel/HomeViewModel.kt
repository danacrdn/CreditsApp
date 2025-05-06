package com.example.creditsapp.presentation.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.repository.UsersRepository
import com.example.creditsapp.domain.model.UserTotalCredits
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.lang.Thread.State

class HomeViewModel(
    private val usersRepository: UsersRepository,
    userPreferences: UserPreferencesRepository
) : ViewModel() {

    val id: StateFlow<Int?> = userPreferences.userId

    @OptIn(ExperimentalCoroutinesApi::class)
    val homeUiState: StateFlow<HomeUiState> = id
        .filterNotNull()
        .flatMapLatest { id -> usersRepository.getUserAndCredits(id).map { HomeUiState(it) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState(UserTotalCredits("", 0))
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val userCredits: UserTotalCredits)