package com.delacruz.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Person
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

// Cada boton de la barra inferior: a que ruta va, su nombre y su icono
private data class ItemBarra(val ruta: String, val etiqueta: String, val icono: ImageVector)

// Los 4 items del dueno (el veterinario tendra 5 en el commit 12)
private val ItemsDueno = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
    ItemBarra(Rutas.CITAS, "Citas", Icons.Filled.CalendarMonth),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

// Barra de navegacion inferior del dueno: Inicio, Mascotas, Citas y Perfil
@Composable
fun BarraDueno(rutaActual: String?, onIr: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        ItemsDueno.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = { onIr(item.ruta) },
                icon = { Icon(imageVector = item.icono, contentDescription = item.etiqueta) },
                label = { Text(text = item.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

// Marco comun de las pantallas del dueno: fondo, barra inferior y el espacio que dejan los bordes.
// Cada pantalla recibe el Modifier con ese espacio ya aplicado
@Composable
fun ConBarraDueno(
    rutaActual: String?,
    onIr: (String) -> Unit,
    contenido: @Composable (Modifier) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraDueno(rutaActual = rutaActual, onIr = onIr) }
    ) { padding ->
        contenido(Modifier.padding(padding))
    }
}