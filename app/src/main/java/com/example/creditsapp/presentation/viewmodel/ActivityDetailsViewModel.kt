package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.database.UserActivity
import com.example.creditsapp.data.repository.ActivitiesRepository
import com.example.creditsapp.data.repository.UserActivitiesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivityDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    activitiesRepository: ActivitiesRepository,
    private val userActivitiesRepository: UserActivitiesRepository,
    userPreferences: UserPreferencesRepository
) : ViewModel() {

    private val activityId: Int = checkNotNull(savedStateHandle["id"]) { "ID not found" }
    private val _message = MutableStateFlow<UiEvent?>(null)
    val message = _message.asStateFlow()

    val id: StateFlow<Int?> = userPreferences.userId

    val activityUiState: StateFlow<ActivityUiState> =
        activitiesRepository.getActivityStream(activityId)
            .map { activity ->
                ActivityUiState(
                    activity = activity,
                    userActivityState = getUserActivityState(activityId, id.value)
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                initialValue = ActivityUiState()
            )

    fun insertActivityUser() {
        viewModelScope.launch {
            val currentUser = id.value

            try {
                currentUser?.let { id ->
                    userActivitiesRepository.insertUserActivity(
                        UserActivity(
                            activityId = activityId,
                            userId = id,
                            completed = false
                        )
                    )
                }
                _message.value = UiEvent.AddSuccess

            } catch (e: Exception) {
                _message.value = UiEvent.AddError
            }
        }
    }

    fun deleteActivityUser() {
        viewModelScope.launch {
            try {
                id.value?.let { userActivitiesRepository.deleteActivityUserByIds(activityId, it) }
                _message.value = UiEvent.DeleteSuccess
            } catch (e: Exception) {
                _message.value = UiEvent.DeleteError
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    private suspend fun getUserActivityState(activityId: Int, value: Int?): UserActivityState {
        val userActivity =
            value?.let { userActivitiesRepository.getActivityStatusForUserStream(activityId, it) }

        return userActivity?.let {
            UserActivityState(isMine = true, isCompleted = it.completed)
        } ?: UserActivityState(isMine = false, isCompleted = false)
    }
}

data class ActivityUiState(
    val activity: Activity? = null,
    val userActivityState: UserActivityState? = null
)

data class UserActivityState(val isMine: Boolean, val isCompleted: Boolean)

enum class UiEvent {
    AddSuccess, AddError, DeleteSuccess, DeleteError
}
