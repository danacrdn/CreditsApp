package com.example.creditsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.creditsapp.model.Activity
import com.example.creditsapp.model.userActivities

/* ViewModel para la pantalla de Mis Créditos.

*/

class ConsultCreditsViewModel : ViewModel() {


    // Función para obtener las actividades del usuario
    fun getActivitiesForUser(userId: Int): List<Activity> {
        return userActivities[userId] ?: emptyList()
    }
}