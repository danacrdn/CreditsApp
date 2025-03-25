package com.example.creditsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.creditsapp.data.Activity
import com.example.creditsapp.data.userActivities

/* ViewModel para la pantalla de Mis Créditos.

*/

class ConsultCreditsViewModel : ViewModel() {


    // Función para obtener las actividades del usuario
    fun getActivitiesForUser(userId: Int): List<Activity> {
        return userActivities[userId] ?: emptyList()
    }
}