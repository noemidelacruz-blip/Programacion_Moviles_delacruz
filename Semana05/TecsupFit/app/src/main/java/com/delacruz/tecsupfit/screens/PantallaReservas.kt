package com.delacruz.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.delacruz.tecsupfit.model.Reserva

/**
 * Pantalla que muestra el listado de reservas activas del usuario y permite cancelarlas.
 */
@Composable
fun PantallaReservas(
    listaReservas: List<Reserva>,
    onEliminarReserva: (Reserva) -> Unit
) {
    // Estado para controlar qué reserva se desea cancelar
    var reservaACancelar by remember { mutableStateOf<Reserva?>(null) }

    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Título principal de la pantalla
        Text(
            text = "Mis Reservas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mensaje si la lista está vacía
        if (listaReservas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes reservas activas en este momento.",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        } else {
            // Lista vertical con las tarjetas de reserva
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaReservas) { reserva ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GrisTarjeta),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ícono decorativo
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(VerdeClaroBg, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventAvailable,
                                    contentDescription = null,
                                    tint = VerdeTecsup
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Información adaptada a los campos de Reserva (claseNombre y horario)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = reserva.claseNombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = reserva.horario,
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = reserva.estado,
                                    color = VerdeTecsup,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Botón para eliminar la reserva
                            IconButton(onClick = { reservaACancelar = reserva }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Cancelar reserva",
                                    tint = Color.Red.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo emergente para confirmar la cancelación
    reservaACancelar?.let { reserva ->
        AlertDialog(
            onDismissRequest = { reservaACancelar = null },
            title = { Text("Cancelar reserva") },
            text = { Text("¿Estás seguro de que deseas cancelar tu cupo para ${reserva.claseNombre}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminarReserva(reserva)
                        reservaACancelar = null
                    }
                ) {
                    Text("Sí, cancelar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaACancelar = null }) {
                    Text("No, mantener", color = Color.Black)
                }
            }
        )
    }
}