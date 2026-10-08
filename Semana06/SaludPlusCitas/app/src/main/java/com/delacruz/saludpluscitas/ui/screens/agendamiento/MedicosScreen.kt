package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: lista de médicos de la especialidad recibida (Repositorio.buscarMedicos),
//       ordenados por calificación, con buscador desde la lupa.
@Composable
fun MedicosScreen(
    navController: NavController,
    especialidadId: Int
) {
    PantallaEnConstruccion(
        titulo = "5. Médicos",
        detalle = "especialidadId = $especialidadId",
        "Elegir fecha y hora del médico 1" to {
            navController.navigate(Rutas.FechaHora.crearRuta(1))
        },
        "Volver" to { navController.popBackStack() }
    )
}