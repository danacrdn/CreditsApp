package com.example.creditsapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled : LiveData<Boolean> = _loginEnabled

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnabled.value = enableLoginButton(email, password)

    }

    // Función de comprobación, devuelve true si ambos valores son válidos

    private fun enableLoginButton(email: String, password: String) = password.length >= 6 && Patterns.EMAIL_ADDRESS.matcher(email).matches()

}