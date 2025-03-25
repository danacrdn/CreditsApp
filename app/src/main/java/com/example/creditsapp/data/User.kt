package com.example.creditsapp.data

data class User(
    val userId: Int,
    val name: String,
    val degreeName: String,
    val email: String
)

val users = listOf(
    User(1, "John Doe", "Ingeniería en Sistemas Computacionales", "john@email.com"),
    User(2, "Jane Smith", "Ingeniería Eléctrica", "jane.sm@email.com"),
    User(3, "Carlos Garcia","Licenciatura en Biología", "garciacarlos@email.com")
)