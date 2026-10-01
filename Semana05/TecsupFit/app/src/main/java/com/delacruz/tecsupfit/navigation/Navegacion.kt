package com.delacruz.tecsupfit.navigation

// Importaciones de Jetpack Compose y Navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.delacruz.tecsupfit.model.ClaseFit
import com.delacruz.tecsupfit.screens.PantallaConfirmacion
import com.delacruz.tecsupfit.screens.PantallaDetalleClase
import com.delacruz.tecsupfit.screens.PantallaPrincipal

/**
 * Objeto que define los identificadores de ruta para la navegación de la aplicación.
 */
object Rutas {
    const val PANTALLA_INICIO = "inicio"
    const val PANTALLA_DETALLE = "detalle"
    const val PANTALLA_CONFIRMACION = "confirmacion"
}

/**
 * Grafo de navegación central que administra el flujo entre pantallas en TECSUP Fit.
 * @param navController Controlador de navegación principal.
 */
@Composable
fun NavegacionApp(navController: NavHostController) {
    // Estado mutable para almacenar la clase seleccionada por el usuario
    var claseSeleccionada by remember {
        mutableStateOf(ClaseFit(1, "Yoga funcional", "7:00 am", "Sala 2"))
    }

    // Configuración del NavHost
    NavHost(
        navController = navController,
        startDestination = Rutas.PANTALLA_INICIO
    ) {
        // Ruta 1: Pantalla Principal (BottomNavigationBar)
        composable(Rutas.PANTALLA_INICIO) {
            PantallaPrincipal(
                onClaseClick = { clase ->
                    claseSeleccionada = clase
                    navController.navigate(Rutas.PANTALLA_DETALLE)
                }
            )
        }

        // Ruta 2: Pantalla de Detalle de Clase
        composable(Rutas.PANTALLA_DETALLE) {
            PantallaDetalleClase(
                clase = claseSeleccionada,
                onBack = { navController.popBackStack() },
                onReservar = {
                    navController.navigate(Rutas.PANTALLA_CONFIRMACION)
                }
            )
        }

        // Ruta 3: Pantalla de Confirmación de Reserva
        composable(Rutas.PANTALLA_CONFIRMACION) {
            PantallaConfirmacion(
                nombre = claseSeleccionada.nombre,
                hora = claseSeleccionada.hora,
                sala = claseSeleccionada.sala,
                onVerReservas = {
                    navController.popBackStack(Rutas.PANTALLA_INICIO, inclusive = false)
                }
            )
        }
    }
}