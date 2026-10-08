package com.delacruz.saludpluscitas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: Reto extra: texto de términos con scroll (o AlertDialog).
//       Barra superior con flecha atrás.
@Composable
fun TerminosScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "15. Términos y condiciones",
        detalle = "",
        "Volver" to { navController.popBackStack() }
    )
}