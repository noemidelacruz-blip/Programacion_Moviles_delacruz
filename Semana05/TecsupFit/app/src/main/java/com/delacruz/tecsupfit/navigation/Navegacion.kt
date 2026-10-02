package com.delacruz.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delacruz.tecsupfit.model.ClaseFit
import com.delacruz.tecsupfit.model.Reserva
import com.delacruz.tecsupfit.screens.PantallaConfirmacion
import com.delacruz.tecsupfit.screens.PantallaDetalleClase
import com.delacruz.tecsupfit.screens.PantallaPrincipal

object Rutas {
    const val PANTALLA_INICIO = "inicio"
    const val PANTALLA_DETALLE = "detalle"
    const val PANTALLA_CONFIRMACION = "confirmacion"
}

@Composable
fun NavegacionApp() {
    val navController = rememberNavController()
    var tabInicial by remember { mutableIntStateOf(0) }

    // 1. Manejar la lista de tipo Reserva en lugar de ClaseFit
    var listaReservas by remember {
        mutableStateOf(
            listOf(
                Reserva(1, "Yoga funcional", "Hoy · 7:00 am - Sala 2", "Confirmada"),
                Reserva(2, "Spinning", "Mañana · 7:30 pm - Sala 3", "Confirmada")
            )
        )
    }

    var claseSeleccionada by remember {
        mutableStateOf(ClaseFit(1, "Yoga funcional", "7:00 am", "Sala 2"))
    }

    NavHost(
        navController = navController,
        startDestination = Rutas.PANTALLA_INICIO
    ) {
        composable(Rutas.PANTALLA_INICIO) {
            PantallaPrincipal(
                tabInicial = tabInicial,
                listaReservas = listaReservas, // Pasamos la lista de objetos Reserva
                onEliminarReserva = { reservaAEliminar ->
                    listaReservas = listaReservas.filter { it.id != reservaAEliminar.id }
                },
                onClaseClick = { clase ->
                    claseSeleccionada = clase
                    navController.navigate(Rutas.PANTALLA_DETALLE)
                }
            )
        }

        composable(Rutas.PANTALLA_DETALLE) {
            PantallaDetalleClase(
                clase = claseSeleccionada,
                onBack = { navController.popBackStack() },
                onReservar = { navController.navigate(Rutas.PANTALLA_CONFIRMACION) }
            )
        }

        composable(Rutas.PANTALLA_CONFIRMACION) {
            PantallaConfirmacion(
                nombre = claseSeleccionada.nombre,
                hora = claseSeleccionada.hora,
                sala = claseSeleccionada.sala,
                onVerReservas = {
                    // 2. Mapear ClaseFit a la estructura Reserva
                    val nuevaReserva = Reserva(
                        id = (listaReservas.maxOfOrNull { it.id } ?: 0) + 1,
                        claseNombre = claseSeleccionada.nombre,
                        horario = "Hoy · ${claseSeleccionada.hora} - ${claseSeleccionada.sala}",
                        estado = "Confirmada"
                    )

                    // 3. Evitar duplicados y agregar la nueva reserva a la lista global
                    if (!listaReservas.any { it.claseNombre == nuevaReserva.claseNombre }) {
                        listaReservas = listaReservas + nuevaReserva
                    }
                    // Redirección hacia la pestaña de Reservas tras confirmar
                    tabInicial = 1
                    navController.popBackStack(Rutas.PANTALLA_INICIO, inclusive = false)
                }
            )
        }
    }
}