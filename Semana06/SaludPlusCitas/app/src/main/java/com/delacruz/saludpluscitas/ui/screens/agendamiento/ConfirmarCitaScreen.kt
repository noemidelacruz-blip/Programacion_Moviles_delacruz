package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: resumen de la cita, motivo de consulta y Repositorio.agendarCita.
//       Al confirmar, navegar a CitaExitosa con popUpTo(Home).
@Composable
fun ConfirmarCitaScreen(
    navController: NavController,
    medicoId: Int,
    fecha: String,
    hora: String
) {
    PantallaEnConstruccion(
        titulo = "7. Confirmar cita",
        detalle = "medicoId = $medicoId, fecha = $fecha, hora = $hora",
        "Agendar cita" to {
            navController.navigate(Rutas.CitaExitosa.crearRuta(1)) {
                popUpTo(Rutas.Home.ruta)
            }
        },
        "Volver" to { navController.popBackStack() }
    )
}