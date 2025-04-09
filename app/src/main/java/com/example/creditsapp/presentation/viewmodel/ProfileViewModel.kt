package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.database.User
import com.example.creditsapp.data.repository.UsersRepository
import com.example.creditsapp.domain.model.users
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(usersRepository: UsersRepository) : ViewModel() {

    private val userId: Int = 1

    val profileUiState: StateFlow<ProfileUiState> =
        usersRepository.getUserStream(userId).map { ProfileUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ProfileUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


}

data class ProfileUiState(val profileData: User? = null)