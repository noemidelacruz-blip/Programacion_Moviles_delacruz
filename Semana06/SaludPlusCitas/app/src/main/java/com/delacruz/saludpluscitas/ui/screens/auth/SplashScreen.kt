package com.delacruz.saludpluscitas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: logo, nombre de la clínica, ilustración y botones "Comenzar" y "Ya tengo una cuenta".
@Composable
fun SplashScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "1. Splash",
        detalle = "",
        "Comenzar" to { navController.navigate(Rutas.Registro.ruta) },
        "Ya tengo una cuenta" to { navController.navigate(Rutas.Login.ruta) }
    )
}