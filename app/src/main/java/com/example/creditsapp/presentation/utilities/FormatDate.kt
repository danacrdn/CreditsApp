package com.example.creditsapp.presentation.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatFecha(fechaIso: String): String {
    val zonedDateTime = ZonedDateTime.parse(fechaIso)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return zonedDateTime.format(formatter)
}
