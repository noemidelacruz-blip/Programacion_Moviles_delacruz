package com.delacruz.saludpluscitas.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: Reto extra: recordatorios generados con map sobre Repositorio.citasDelUsuario.
@Composable
fun NotificacionesScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "14. Notificaciones",
        detalle = "",
        "Volver" to { navController.popBackStack() }
    )
}