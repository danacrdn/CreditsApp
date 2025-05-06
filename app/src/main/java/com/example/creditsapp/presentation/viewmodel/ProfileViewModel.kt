package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.database.User
import com.example.creditsapp.data.repository.UsersRepository
import com.example.creditsapp.domain.model.EditableProfileData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val usersRepository: UsersRepository,
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {

    private val _navigateToLogin = MutableSharedFlow<Boolean>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _editableData = MutableStateFlow(EditableProfileData())
    val editableData: StateFlow<EditableProfileData> = _editableData.asStateFlow()

    private val _originalData = MutableStateFlow<User?>(null)


    val id: StateFlow<Int?> = userPreferences.userId

    @OptIn(ExperimentalCoroutinesApi::class)
    val profileUiState: StateFlow<ProfileUiState> = id
        .filterNotNull()
        .flatMapLatest { id ->
            usersRepository.getUserStream(id).map { ProfileUiState(it) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ProfileUiState()
        )

    /* This function activates editing mode. Once active, it assigns the original data
    * to observable value _editableData, which is of type EditableProfileData. This data class temporarily
    * stores the values before they are updated */
    fun startEditing() {
        _isEditing.value = true
        val currentData = profileUiState.value.profileData
        _originalData.value = currentData
        currentData?.let { data ->
            _editableData.value = EditableProfileData(
                firstName = data.firstName,
                lastName = data.lastName,
                degreeName = data.degreeName,
                email = data.email,
                password = data.password
            )
        }
    }

    /* Function to update the values. This function updates a specific value from _editableData
    * (Editable data is the new data, a temporary model to edit an then update the database).
    * depending on value name. .copy() creates a new instance of the object
    * with the modified field and assigns it again. */

    fun updateValue(fieldName: String, value: String) {
        _editableData.value =
            when (fieldName) {
                "firstName" -> _editableData.value.copy(firstName = value)
                "lastName" -> _editableData.value.copy(lastName = value)
                "degreeName" -> _editableData.value.copy(degreeName = value)
                "email" -> _editableData.value.copy(email = value)
                "password" -> _editableData.value.copy(password = value)
                else -> _editableData.value
            }
    }

    fun saveEditing() {
        viewModelScope.launch {
            // change isSaving to true.
            _isSaving.value = true

            // then, create a new val with the new values, that will replace the old ones.
            val updatedProfile = _editableData.value.let { editable ->
                profileUiState.value.profileData?.copy(
                    firstName = editable.firstName,
                    lastName = editable.lastName,
                    degreeName = editable.degreeName,
                    email = editable.email,
                    password = editable.password
                )
            }

            // now, use the function update user to update the database.
            updatedProfile?.let { updatedData ->
                usersRepository.updateUser(updatedData)
                // once it updates, the saving is false again and is editing too.
                _isSaving.value = false
                _isEditing.value = false
            }
        }
    }

    /* This function cancels all the changes and turns off the editing mode.*/

    fun cancelEditing() {
        _isEditing.value = false
        _originalData.value.let {
            if (it != null) {
                _editableData.value = EditableProfileData(
                    firstName = it.firstName,
                    lastName = it.lastName,
                    degreeName = it.degreeName,
                    email = it.email,
                    password = it.password
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            userPreferences.removeUserId()
            _navigateToLogin.emit(true)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class ProfileUiState(val profileData: User? = null)