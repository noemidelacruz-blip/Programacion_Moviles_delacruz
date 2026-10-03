package com.delacruz.tecsupstore.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Menú lateral de navegación (ModalDrawerSheet) que contiene la información
 * de perfil de la estudiante y las opciones principales de navegación.
 */
@Composable
fun AppDrawer(
    rutaActual: String,
    alSeleccionarRuta: (String) -> Unit
) {
    ModalDrawerSheet {
        // Cabecera con avatar y datos del usuario (Noemi De La Cruz)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFE8DEF8), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ND",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4A148C)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Noemi De La Cruz",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "noemi.delacruz@tecsup.edu.pe",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        // Opciones principales del menú de navegación
        NavigationDrawerItem(
            label = { Text("Inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            selected = rutaActual == "inicio",
            onClick = { alSeleccionarRuta("inicio") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) },
            selected = rutaActual == "pedidos",
            onClick = { alSeleccionarRuta("pedidos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Favoritos") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            selected = rutaActual == "favoritos",
            onClick = { alSeleccionarRuta("favoritos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            selected = rutaActual == "perfil",
            onClick = { alSeleccionarRuta("perfil") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text("Cerrar sesion") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            selected = rutaActual == "cerrar_sesion",
            onClick = { alSeleccionarRuta("cerrar_sesion") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}