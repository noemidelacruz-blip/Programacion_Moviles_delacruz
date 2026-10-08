package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: días de la semana, LazyVerticalGrid de horarios disponibles
//       (Repositorio.horariosDisponibles) y botón Continuar.
@Composable
fun FechaHoraScreen(
    navController: NavController,
    medicoId: Int
) {
    PantallaEnConstruccion(
        titulo = "6. Fecha y hora",
        detalle = "medicoId = $medicoId",
        "Continuar" to {
            navController.navigate(Rutas.ConfirmarCita.crearRuta(medicoId, "2026-10-13", "09:30"))
        },
        "Volver" to { navController.popBackStack() }
    )
}