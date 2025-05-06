package com.example.creditsapp.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.UserPreferencesRepository
import com.example.creditsapp.data.repository.UsersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val usersRepository: UsersRepository,
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {

    val userId: StateFlow<Int?> = userPreferences.userId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnabled.value = enableLoginButton(email, password)

    }
    // Función de comprobación, devuelve true si ambos valores son válidos

    private fun enableLoginButton(email: String, password: String) =
        password.length >= 6 && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun validateCredentials(email: String, inputPassword: String, onResult: (Boolean) -> Unit) {
        // obtener el usuario correspondiente por medio del email ingresado.
        viewModelScope.launch {
            val user = usersRepository.getUserByEmailStream(email).firstOrNull()
            val isValid = user?.password == inputPassword

            if (isValid) {
                userPreferences.saveUserId(user!!.id)
            }
            onResult(isValid)
        }
    }
}