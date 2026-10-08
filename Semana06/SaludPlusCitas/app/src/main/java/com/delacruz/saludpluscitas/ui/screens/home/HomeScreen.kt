package com.delacruz.saludpluscitas.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: saludo con el nombre del usuario, grid 2x2 de accesos rápidos,
//       LazyRow de especialidades destacadas y NavigationBar.
@Composable
fun HomeScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "3. Inicio",
        detalle = "",
        "Agendar cita" to { navController.navigate(Rutas.Especialidades.ruta) },
        "Mis citas" to { navController.navigate(Rutas.MisCitas.ruta) },
        "Mis datos" to { navController.navigate(Rutas.Perfil.ruta) },
        "Resultados" to { navController.navigate(Rutas.Resultados.ruta) },
        "Notificaciones" to { navController.navigate(Rutas.Notificaciones.ruta) }
    )
}