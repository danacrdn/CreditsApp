package com.example.creditsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.creditsapp.data.User
import com.example.creditsapp.data.users

class ProfileViewModel : ViewModel(){
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user


    // Función ppara obtener los datos del usuario y mostrarlos
    // Por ahora utilizo datos ficticios
    private fun fetchUserData() {
        _user.value = users.last()
    }

    // Función para obtener el modo oscuro
    private fun fetchDarkMode(){

    }

    // Función para guardar la configuración del modo oscuro
    private fun saveDarkModeStatus(){
        // Más tarde, guardaré localmente esta configuración en una base de datos:
        // Por ejemplo: "dark_mode", estado
    }

    init {
        fetchUserData()
    }


}