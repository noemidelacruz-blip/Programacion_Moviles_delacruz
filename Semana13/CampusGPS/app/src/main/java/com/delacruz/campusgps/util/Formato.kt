package com.delacruz.campusgps.util

import java.util.Locale
import kotlin.math.roundToInt

//Convierte la distancia texto fácil de leer: metros si es corto, kilómetros si es largo.
fun formatearDistancia(metros: Float): String {
    return if (metros < 1000f) {
        "${metros.roundToInt()} m"
    } else {
        // Se usa Locale.US para que el decimal salga siempre con punto: 1.5 km
        String.format(Locale.US, "%.1f km", metros / 1000f)
    }
}
