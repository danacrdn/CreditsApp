package com.example.creditsapp.presentation.viewmodel.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.ActividadesRepository
import com.example.creditsapp.data.repository.AlumnoActividadRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

@RequiresApi(Build.VERSION_CODES.O)
class ActivityDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    alumnoActividadRepository: AlumnoActividadRepository,
    actividadRepository: ActividadesRepository,
    userPreferences: UserPreferencesRepository
) : ViewModel() {

    private val activityId: Int = checkNotNull(savedStateHandle["id"]) { "ID not found" }

    private val _effect = MutableSharedFlow<ActivityDetailsEffect>()
    val effect: SharedFlow<ActivityDetailsEffect> = _effect.asSharedFlow()

    private val _uiState = MutableStateFlow(ActivityDetailsUiState())
    val uiState: StateFlow<ActivityDetailsUiState> = _uiState.asStateFlow()

    private val handler = ActivityDetailsEffectHandler(
        alumnoActividadRepository,
        actividadRepository,
        userPreferences,
        viewModelScope,
        _effect,
        activityId
    ) { intent -> onIntent(intent) }

    init {
        onIntent(ActivityDetailsIntent.LoadActivityDetails)
    }

    fun onIntent(intent: ActivityDetailsIntent) {
        val (newState, _) = ActividadDetailsReducer.reducer(_uiState.value, intent)
        _uiState.value = newState
        handler.handle(intent)
    }
}

