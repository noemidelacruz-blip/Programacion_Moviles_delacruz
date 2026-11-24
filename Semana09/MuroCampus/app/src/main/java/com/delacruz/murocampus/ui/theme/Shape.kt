package com.delacruz.murocampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Formas del rediseño. Las tarjetas y los campos comparten 24 dp de esquina;
// los chips y los botones son píldoras; el botón + es un cuadrado de 62 dp.
val FormaTarjeta = RoundedCornerShape(24.dp)
val FormaCampo = RoundedCornerShape(24.dp)
val FormaPildora = RoundedCornerShape(percent = 50)
val FormaBotonAgregar = RoundedCornerShape(22.dp)
val TamanioBotonAgregar = 62.dp

/** Formas que Material 3 usa por defecto en sus componentes. */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = FormaTarjeta,
    large = FormaTarjeta,
    extraLarge = RoundedCornerShape(28.dp)
)
