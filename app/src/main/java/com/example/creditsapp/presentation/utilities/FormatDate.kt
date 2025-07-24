package com.example.creditsapp.presentation.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun formatDate(date: Date): String {
    val locale = Locale("es", "ES")
    val formated = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault())
    return formated.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatFecha(fechaIso: String): String {
    val zonedDateTime = ZonedDateTime.parse(fechaIso)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return zonedDateTime.format(formatter)
}
