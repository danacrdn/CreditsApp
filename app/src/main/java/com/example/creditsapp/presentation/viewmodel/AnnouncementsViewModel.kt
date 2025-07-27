package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AvisoRepository
import com.example.creditsapp.domain.model.Aviso
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnnouncementsViewModel(private val avisoRepository: AvisoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AnnouncementsUiState>(AnnouncementsUiState.Loading)
    val uiState: StateFlow<AnnouncementsUiState> = _uiState

    init {
        getAvisos()

    }

    private fun getAvisos() {
        viewModelScope.launch {
            runCatching { avisoRepository.getAvisos() }
                .fold(
                    onSuccess = { _uiState.value = AnnouncementsUiState.Success(it) },
                    onFailure = { _uiState.value = AnnouncementsUiState.Error }
                )
        }
    }
}

sealed class AnnouncementsUiState {
    data class Success(
        val avisos: List<Aviso>
    ) : AnnouncementsUiState()

    object Loading : AnnouncementsUiState()
    object Error : AnnouncementsUiState()
}