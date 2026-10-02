package com.delacruz.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delacruz.tecsupfit.model.ClaseFit
import com.delacruz.tecsupfit.screens.PantallaConfirmacion
import com.delacruz.tecsupfit.screens.PantallaDetalleClase
import com.delacruz.tecsupfit.screens.PantallaPrincipal

/**
 * Objeto central que define los identificadores de ruta para la navegación de la aplicación.
 */
object Rutas {
    // Constante para la ruta de la pantalla principal de inicio
    const val PANTALLA_INICIO = "inicio"
    // Constante para la ruta de la pantalla de detalle de clase
    const val PANTALLA_DETALLE = "detalle"
    // Constante para la ruta de la pantalla de confirmación de reserva
    const val PANTALLA_CONFIRMACION = "confirmacion"
}

/**
 * Grafo de navegación central que administra el flujo entre pantallas en TECSUP Fit.
 */
@Composable
fun NavegacionApp() {
    // Creación e inicialización del NavController con rememberNavController para gestionar la navegación internamente
    val navController = rememberNavController()

    // Estado mutable para almacenar y compartir la clase seleccionada entre las vistas
    var claseSeleccionada by remember {
        mutableStateOf(ClaseFit(1, "Yoga funcional", "7:00 am", "Sala 2"))
    }

    // Contenedor principal NavHost asignando el controlador e indicando la ruta inicial
    NavHost(
        navController = navController,
        startDestination = Rutas.PANTALLA_INICIO
    ) {
        // Ruta 1: Pantalla Principal (Contiene la navegación por pestañas de Inicio, Reservas y Perfil)
        composable(Rutas.PANTALLA_INICIO) {
            PantallaPrincipal(
                onClaseClick = { clase ->
                    // Guarda la clase seleccionada en el estado local
                    claseSeleccionada = clase
                    // Navega hacia la pantalla de detalle
                    navController.navigate(Rutas.PANTALLA_DETALLE)
                }
            )
        }

        // Ruta 2: Pantalla de Detalle de la Clase Seleccionada
        composable(Rutas.PANTALLA_DETALLE) {
            PantallaDetalleClase(
                clase = claseSeleccionada,
                onBack = {
                    // Regresa a la pantalla anterior en la pila de navegación
                    navController.popBackStack()
                },
                onReservar = {
                    // Navega a la pantalla de confirmación tras realizar la reserva
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
                    // Limpia la pila de navegación y retorna a la pantalla de inicio
                    navController.popBackStack(Rutas.PANTALLA_INICIO, inclusive = false)
                }
            )
        }
    }
}