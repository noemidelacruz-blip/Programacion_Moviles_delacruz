package com.delacruz.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

// Cada boton de la barra inferior del veterinario: a que ruta va, su nombre y su icono
private data class ItemBarraVet(val ruta: String, val etiqueta: String, val icono: ImageVector)

// Los 5 items del veterinario (el dueno tiene 4)
private val ItemsVet = listOf(
    ItemBarraVet(Rutas.PANEL, "Panel", Icons.Filled.BarChart),
    ItemBarraVet(Rutas.AGENDA, "Agenda", Icons.Filled.CalendarMonth),
    ItemBarraVet(Rutas.PACIENTES, "Pacientes", Icons.Filled.Pets),
    ItemBarraVet(Rutas.SERVICIOS, "Servicios", Icons.Filled.LocalOffer),
    ItemBarraVet(Rutas.AJUSTES, "Ajustes", Icons.Filled.Settings)
)

// Barra de navegacion inferior del veterinario: Panel, Agenda, Pacientes, Servicios y Ajustes
@Composable
fun BarraVet(rutaActual: String?, onIr: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        ItemsVet.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = { onIr(item.ruta) },
                icon = { Icon(imageVector = item.icono, contentDescription = item.etiqueta) },
                label = { Text(text = item.etiqueta, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

// Marco comun de las pantallas del veterinario: fondo, barra inferior y el espacio que dejan los bordes.
// Cada pantalla recibe el Modifier con ese espacio ya aplicado
@Composable
fun ConBarraVet(
    rutaActual: String?,
    onIr: (String) -> Unit,
    contenido: @Composable (Modifier) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraVet(rutaActual = rutaActual, onIr = onIr) }
    ) { padding ->
        contenido(Modifier.padding(padding))
    }
}