package com.delacruz.saludpluscitas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: LazyColumn con Repositorio.citasDelUsuario y mensaje de lista vacía.
@Composable
fun MisCitasScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "10. Mis citas",
        detalle = "",
        "Ver detalle de la cita 1" to {
            navController.navigate(Rutas.DetalleCita.crearRuta(1))
        },
        "Volver" to { navController.popBackStack() }
    )
}