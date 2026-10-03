package com.delacruz.tecsupstore.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.delacruz.tecsupstore.screens.HomeScreen
import kotlinx.coroutines.launch

/**
 * Estructura contenedora principal de la aplicación.
 * Administra el estado de la navegación lateral y la TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var rutaActual by remember { mutableStateOf("inicio") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                rutaActual = rutaActual,
                alSeleccionarRuta = { nuevaRuta ->
                    rutaActual = nuevaRuta
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(text = "TECSUP Store", style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = "Mas vendidos",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú de navegación", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF4A148C),
                        titleContentColor = Color.White
                    )
                )
            }
        ) { paddingValores ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValores)
            ) {
                when (rutaActual) {
                    "inicio" -> HomeScreen()
                    else -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Pantalla: $rutaActual")
                    }
                }
            }
        }
    }
}