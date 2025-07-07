package com.example.creditsapp.presentation.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(date: Date): String {
    val locale = Locale("es", "ES")
    val formated = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault())
    return formated.format(date)
}