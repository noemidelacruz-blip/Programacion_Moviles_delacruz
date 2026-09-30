package com.delacruz.clinicasalud.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    contenidoPantalla: @Composable (onClickAbrirDrawer: () -> Unit) -> Unit
) {
    val contexto = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var destinoSeleccionado by remember { mutableStateOf("Inicio") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFE8DEF8)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text("JP", fontWeight = FontWeight.Bold, color = Color(0xFF4A148C))
                            }
                        }
                    }
                    Text(
                        text = "Juan Pérez",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Paciente",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") },
                    selected = destinoSeleccionado == "Inicio",
                    onClick = {
                        destinoSeleccionado = "Inicio"
                        coroutineScope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    label = { Text("Mis citas") },
                    selected = destinoSeleccionado == "Mis citas",
                    onClick = {
                        destinoSeleccionado = "Mis citas"
                        coroutineScope.launch { drawerState.close() }
                        Toast.makeText(contexto, "Navegando a Mis Citas", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = null) },
                    label = { Text("Historial médico") },
                    selected = destinoSeleccionado == "Historial médico",
                    onClick = {
                        destinoSeleccionado = "Historial médico"
                        coroutineScope.launch { drawerState.close() }
                        Toast.makeText(contexto, "Sección Historial Médico", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    ) {
        contenidoPantalla {
            coroutineScope.launch { drawerState.open() }
        }
    }
}