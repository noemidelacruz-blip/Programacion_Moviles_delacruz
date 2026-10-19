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
// Convierte una fecha "aaaa-MM-dd" en "dd mmm": "12 oct" (fecha del historial clinico)
fun fechaDiaMes(fecha: String): String {
    val f = LocalDate.parse(fecha)
    val mes = f.month.getDisplayName(TextStyle.SHORT, ES).replace(".", "")
    return String.format(Locale.US, "%02d", f.dayOfMonth) + " " + mes
}

// Precio como texto: "S/ 60" o, con decimales = true, "S/ 45.00"
fun textoPrecio(precio: Double, decimales: Boolean = false): String =
    if (decimales || precio % 1.0 != 0.0) "S/ " + String.format(Locale.US, "%.2f", precio)
    else "S/ " + precio.toInt()

// Convierte una fecha "aaaa-MM-dd" en el dia de la semana y el numero: "Mie 21"
fun fechaDia(fecha: String): String {
    val f = LocalDate.parse(fecha)
    val dia = f.dayOfWeek.getDisplayName(TextStyle.SHORT, ES)
        .replace(".", "")
        .replaceFirstChar { it.uppercase() }
    return "$dia ${f.dayOfMonth}"
}