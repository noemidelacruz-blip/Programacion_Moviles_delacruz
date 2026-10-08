package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: mensaje de éxito, resumen de la cita (Repositorio.obtenerCita)
//       y botones "Ver mis citas" e "Ir al inicio".
@Composable
fun CitaExitosaScreen(
    navController: NavController,
    citaId: Int
) {
    PantallaEnConstruccion(
        titulo = "9. Cita agendada",
        detalle = "citaId = $citaId",
        "Ver mis citas" to {
            navController.navigate(Rutas.MisCitas.ruta) {
                popUpTo(Rutas.Home.ruta)
            }
        },
        "Ir al inicio" to { navController.popBackStack(Rutas.Home.ruta, inclusive = false) }
    )
}