package com.delacruz.tecsupfit.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.delacruz.tecsupfit.model.ClaseFit

/**
 * Pantalla contenedora principal que administra la navegación inferior de 4 secciones.
 */
@Composable
fun PantallaPrincipal(
    onClaseClick: (ClaseFit) -> Unit
) {
    // Estado mutable para el índice de la pestaña activa (0: Inicio, 1: Reservas, 2: Rutinas, 3: Perfil)
    var indiceSeleccionado by remember { mutableIntStateOf(0) }

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
                1 -> PantallaReservas()
                2 -> PantallaRutinas()
                3 -> PantallaPerfil()
            }
        }
    }
}