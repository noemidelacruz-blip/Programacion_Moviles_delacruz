package com.delacruz.huellitasvet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.ui.graphics.vector.ImageVector

// Convierte la clave de icono guardada en la tabla servicios (heart, syringe, scissors)
// en el icono de Material que se muestra en pantalla
fun iconoDeServicio(clave: String): ImageVector = when (clave) {
    "syringe" -> Icons.Filled.Vaccines
    "scissors" -> Icons.Filled.ContentCut
    else -> Icons.Filled.Favorite
}