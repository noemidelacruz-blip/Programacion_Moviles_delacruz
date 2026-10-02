package com.delacruz.tecsupfit.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.delacruz.tecsupfit.model.ClaseFit
import com.delacruz.tecsupfit.model.Reserva

/**
 * Pantalla contenedora principal que administra la navegación inferior de 4 secciones.
 */
@Composable
fun PantallaPrincipal(
    tabInicial: Int = 0,
    listaReservas: List<Reserva> = emptyList(), // <-- Recibe la lista de reservas
    onEliminarReserva: (Reserva) -> Unit = {},  // <-- Recibe el callback para eliminar
    onClaseClick: (ClaseFit) -> Unit
) {
    // Estado mutable para el índice de la pestaña activa
    var indiceSeleccionado by remember { mutableIntStateOf(tabInicial) }

    // Sincroniza la pestaña cuando tabInicial cambia desde la navegación
    LaunchedEffect(tabInicial) {
        indiceSeleccionado = tabInicial
    }

    // Lista con las 4 pestañas requeridas
    val opcionesNavegacion = listOf("Inicio", "Reservas", "Rutinas", "Perfil")

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                opcionesNavegacion.forEachIndexed { indice, titulo ->
                    NavigationBarItem(
                        selected = indiceSeleccionado == indice,
                        onClick = { indiceSeleccionado = indice },
                        label = {
                            Text(
                                text = titulo,
                                color = if (indiceSeleccionado == indice) Color(0xFF006837) else Color.Gray
                            )
                        },
                        icon = { },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(0xFFE0F2E9)
                        )
                    )
                }
            }
        }
    ) { paddingInterno ->
        // Renderizado de pantallas según la pestaña elegida
        Surface(modifier = Modifier.padding(paddingInterno)) {
            when (indiceSeleccionado) {
                0 -> PantallaInicio(onClaseClick = onClaseClick)
                1 -> PantallaReservas(
                    listaReservas = listaReservas,
                    onEliminarReserva = onEliminarReserva
                )
                2 -> PantallaRutinas()
                3 -> PantallaPerfil()
            }
        }
    }
}