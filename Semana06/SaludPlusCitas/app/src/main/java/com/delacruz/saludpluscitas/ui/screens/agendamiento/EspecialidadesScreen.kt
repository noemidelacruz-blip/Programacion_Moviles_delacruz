package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: buscador y LazyColumn de especialidades con búsqueda en tiempo real
//       (Repositorio.buscarEspecialidades).
@Composable
fun EspecialidadesScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "4. Especialidades",
        detalle = "",
        "Ver médicos de Medicina General" to {
            navController.navigate(Rutas.Medicos.crearRuta(1))
        },
        "Volver" to { navController.popBackStack() }
    )
}