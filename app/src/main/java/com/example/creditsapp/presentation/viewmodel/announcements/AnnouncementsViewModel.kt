package com.example.creditsapp.presentation.viewmodel.announcements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.AvisoRepository
import com.example.creditsapp.domain.model.Aviso
import com.example.creditsapp.presentation.utilities.UiState
import com.example.creditsapp.presentation.utilities.fetchAsFlow
import com.example.creditsapp.presentation.utilities.toUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AnnouncementsViewModel(private val avisoRepository: AvisoRepository) : ViewModel() {

    val uiState: StateFlow<UiState<List<Aviso>>> = fetchAsFlow { avisoRepository.getAvisos() }
        .map { result -> result.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UiState.Loading
        )
}