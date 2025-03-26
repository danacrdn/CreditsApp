package com.example.creditsapp.model

import com.example.creditsapp.R

data class Activity(
    val id: Int,
    val image: Int,
    val name: String,
    val date: String,
    val hour: String?,
    val place: String,
    val spots: String,
    val creditValue: Int,
    val completed: Boolean
)

val activities = listOf(
    Activity(
        id = 1,
        image = R.drawable.activity,
        name = "Verano de la Investigación Científica",
        date = "17 de marzo, 2025",
        hour = "0:00",
        place = "Centro de Investigación",
        spots = "0/10",
        creditValue =  1,
        completed = true
    ),
    Activity(
        id = 2,
        image = R.drawable.activity,
        name = "Curso MOOC",
        date = "20 de marzo, 2025",
        hour = "0:00",
        place = "En línea",
        spots = "0/20",
        creditValue =  1,
        completed = true
    ),
    Activity(
        id = 3,
        image = R.drawable.activity,
        name = "Tutoría Calculo Diferencial",
        date = "1 de marzo, 2025",
        hour = "0:00",
        place = "En línea",
        spots = "0/20",
        creditValue =  1,
        completed = true
    )
)
