package com.delacruz.huellitasvet.ui.components

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private val ES = Locale.forLanguageTag("es-PE")

// Convierte una fecha "aaaa-MM-dd" en texto corto: "Mie 21 oct"
fun fechaCorta(fecha: String): String {
    val f = LocalDate.parse(fecha)
    val dia = f.dayOfWeek.getDisplayName(TextStyle.SHORT, ES)
        .replace(".", "")
        .replaceFirstChar { it.uppercase() }
    val mes = f.month.getDisplayName(TextStyle.SHORT, ES).replace(".", "")
    return "$dia ${f.dayOfMonth} $mes"
}

// Edad en anios a partir del anio de nacimiento (anio actual - anioNacimiento)
fun textoEdad(anioNacimiento: Int): String {
    val anios = (LocalDate.now().year - anioNacimiento).coerceAtLeast(0)
    return if (anios == 1) "1 año" else "$anios años"
}