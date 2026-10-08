package com.delacruz.saludpluscitas.ui.screens.perfil

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: datos del usuario en sesión y botón "Cerrar sesión" (Repositorio.cerrarSesion).
@Composable
fun PerfilScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "11. Perfil / Mis datos",
        detalle = "",
        "Cerrar sesión" to {
            navController.navigate(Rutas.Splash.ruta) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    )
}