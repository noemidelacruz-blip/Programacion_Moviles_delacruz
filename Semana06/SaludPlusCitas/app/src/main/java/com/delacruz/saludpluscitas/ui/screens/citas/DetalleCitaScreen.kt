package com.delacruz.saludpluscitas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: Reto extra: datos completos de la cita (Repositorio.obtenerCita).
//       Botón "Cancelar cita" con AlertDialog de confirmación y Repositorio.cancelarCita.
@Composable
fun DetalleCitaScreen(
    navController: NavController,
    citaId: Int
) {
    PantallaEnConstruccion(
        titulo = "12. Detalle de cita",
        detalle = "citaId = $citaId",
        "Volver" to { navController.popBackStack() }
    )
}