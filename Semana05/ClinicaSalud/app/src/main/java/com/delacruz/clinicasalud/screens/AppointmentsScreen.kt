package com.delacruz.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.delacruz.clinicasalud.navigation.Cita

/**
 * Composable que gestiona y despliega la lista interactiva de citas médicas.
 * Permite visualizar el estado de cada reserva y solicitar la cancelación mediante un diálogo modal.
 *
 * @param onMenuClick Callback para desplegar el drawer o menú de navegación lateral.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    onMenuClick: () -> Unit
) {
    // Estado mutable de la lista de citas para reflejar cambios en la interfaz tras una cancelación
    var listaCitas by remember {
        mutableStateOf(
            listOf(
                Cita("Dra. Ana Torres", "Jue 26", "10:30", "Confirmada"),
                Cita("Dr. Luis Vega", "15 Ene", "11:00", "Completada")
            )
        )
    }

    // Estado reactivo para almacenar la cita seleccionada que se intenta cancelar
    var citaSeleccionadaParaCancelar by remember { mutableStateOf<Cita?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // Accion para abrir el menu hamburguesa lateral
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menú lateral"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Renderizado del listado optimizado con LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaCitas) { cita ->
                // Renderizado individual enviando el callback para abrir el dialogo
                TarjetaCitaItem(
                    cita = cita,
                    onSolicitarCancelar = { citaSeleccionadaParaCancelar = cita }
                )
            }
        }

        // Diálogo modal de confirmacion para la cancelacion de cita
        citaSeleccionadaParaCancelar?.let { cita ->
            AlertDialog(
                onDismissRequest = { citaSeleccionadaParaCancelar = null },
                title = {
                    Text(
                        text = "¿Cancelar cita?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Text("¿Estás seguro de que deseas cancelar esta cita con ${cita.doctorNombre}?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Actualizacion del estado de la cita seleccionada a "Cancelada"
                            listaCitas = listaCitas.map { item ->
                                if (item == cita) item.copy(estado = "Cancelada") else item
                            }
                            citaSeleccionadaParaCancelar = null
                        }
                    ) {
                        Text(
                            text = "Cancelar cita",
                            color = Color(0xFFD32F2F), // Rojo representativo para acciones destructivas
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { citaSeleccionadaParaCancelar = null }) {
                        Text(
                            text = "Volver",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                shape = RoundedCornerShape(28.dp),
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

/**
 * Composable que renderiza la tarjeta contenedora de la información de la cita.
 *
 * @param cita Objeto estructurado de la cita médica.
 * @param onSolicitarCancelar Evento disparado al accionar el botón de cancelación.
 */
@Composable
fun TarjetaCitaItem(
    cita: Cita,
    onSolicitarCancelar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono indicativo de la cita
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 12.dp)
                )

                // Detalles informativos de profesional y horario
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cita.doctorNombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${cita.fecha} - ${cita.hora}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Determinacion dinamica del esquema cromático de la etiqueta de estado
                val (colorFondo, colorTexto) = when (cita.estado) {
                    "Confirmada" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32)) // Verde para confirmadas
                    "Cancelada"  -> Pair(Color(0xFFFFEBEE), Color(0xFFD32F2F)) // Rojo para canceladas
                    else         -> Pair(Color(0xFFE0E0E0), Color(0xFF616161)) // Gris por defecto/completadas
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = colorFondo
                ) {
                    Text(
                        text = cita.estado,
                        color = colorTexto,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Muestra la opcion interactiva de cancelación únicamente si la cita esta Confirmada
            if (cita.estado == "Confirmada") {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onSolicitarCancelar,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Cancelar cita",
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}