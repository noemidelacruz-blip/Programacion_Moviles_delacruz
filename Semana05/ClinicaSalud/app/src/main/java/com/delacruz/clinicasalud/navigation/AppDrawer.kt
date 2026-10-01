package com.delacruz.clinicasalud.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * Menu lateral deslizable (Navigation Drawer) personalizado con indicadores circulares.
 */
@Composable
fun AppDrawer(
    onNavegarA: (String) -> Unit,
    contenidoPantalla: @Composable (onClickAbrirDrawer: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var destinoSeleccionado by remember { mutableStateOf("Inicio") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Cabecera con avatar e informacion del paciente
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
                                Text("NC", fontWeight = FontWeight.Bold, color = Color(0xFF4A148C))
                            }
                        }
                    }
                    Text(
                        text = "Noemí de la Cruz",
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

                // Opcion 1: Inicio
                NavigationDrawerItem(
                    icon = {
                        RadioButton(
                            selected = destinoSeleccionado == "Inicio",
                            onClick = null
                        )
                    },
                    label = { Text("Inicio") },
                    selected = destinoSeleccionado == "Inicio",
                    onClick = {
                        destinoSeleccionado = "Inicio"
                        coroutineScope.launch { drawerState.close() }
                        onNavegarA(Screen.Home.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                // Opcion 2: Mis citas
                NavigationDrawerItem(
                    icon = {
                        RadioButton(
                            selected = destinoSeleccionado == "Mis citas",
                            onClick = null
                        )
                    },
                    label = { Text("Mis citas") },
                    selected = destinoSeleccionado == "Mis citas",
                    onClick = {
                        destinoSeleccionado = "Mis citas"
                        coroutineScope.launch { drawerState.close() }
                        onNavegarA(Screen.Appointments.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                // Opcion 3: Historial medico
                NavigationDrawerItem(
                    icon = {
                        RadioButton(
                            selected = destinoSeleccionado == "Historial médico",
                            onClick = null
                        )
                    },
                    label = { Text("Historial médico") },
                    selected = destinoSeleccionado == "Historial médico",
                    onClick = {
                        destinoSeleccionado = "Historial médico"
                        coroutineScope.launch { drawerState.close() }
                        onNavegarA(Screen.MedicalHistory.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                // Opcion 4: Perfil
                NavigationDrawerItem(
                    icon = {
                        RadioButton(
                            selected = destinoSeleccionado == "Perfil",
                            onClick = null
                        )
                    },
                    label = { Text("Perfil") },
                    selected = destinoSeleccionado == "Perfil",
                    onClick = {
                        destinoSeleccionado = "Perfil"
                        coroutineScope.launch { drawerState.close() }
                        onNavegarA(Screen.Profile.route)
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