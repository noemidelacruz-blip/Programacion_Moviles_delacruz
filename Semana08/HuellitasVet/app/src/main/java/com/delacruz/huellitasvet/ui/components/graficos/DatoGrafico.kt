package com.delacruz.huellitasvet.ui.components.graficos

import androidx.compose.ui.graphics.Color

// Un dato para los graficos del panel: nombre, cantidad y color.
// Los ViewModels convierten los datos de Room a este formato
data class DatoGrafico(
    val etiqueta: String,
    val valor: Int,
    val color: Color
)