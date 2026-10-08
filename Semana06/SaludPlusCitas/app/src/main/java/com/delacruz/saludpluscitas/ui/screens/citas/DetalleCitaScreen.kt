package com.delacruz.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.components.EstadoVacio
import com.delacruz.saludpluscitas.ui.components.FilaDato
import com.delacruz.saludpluscitas.ui.components.ResumenMedico
import com.delacruz.saludpluscitas.ui.components.formatearFecha
import com.delacruz.saludpluscitas.ui.components.rangoHora
import com.delacruz.saludpluscitas.ui.theme.RojoTexto

// Muestra los datos completos de una cita y permite cancelarla.
@Composable
fun DetalleCitaScreen(
    navController: NavController,
    citaId: Int
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }

    // Controla si se muestra el diálogo de confirmación.
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Detalle de cita",
                onAtras = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        if (cita == null || medico == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                EstadoVacio(
                    mensaje = "No se encontró la cita",
                    icono = Icons.Default.EventBusy
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    ResumenMedico(medico = medico, mostrarCmp = true)

                    Spacer(modifier = Modifier.height(12.dp))

                    FilaDato(Icons.Default.CalendarMonth, "Fecha", formatearFecha(cita.fecha))
                    FilaDato(Icons.Default.Schedule, "Hora", rangoHora(cita.hora))
                    FilaDato(Icons.Default.LocalHospital, "Tipo de atención", cita.tipo)
                    FilaDato(Icons.Default.LocationOn, "Dirección", medico.direccion)
                    FilaDato(
                        Icons.Default.Description,
                        "Motivo de consulta",
                        cita.motivo.ifBlank { "Sin motivo indicado" }
                    )
                }

                OutlinedButton(
                    onClick = { mostrarConfirmacion = true },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, RojoTexto),
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Cancelar cita",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RojoTexto
                    )
                }
            }
        }
    }

    // Diálogo de confirmación antes de cancelar la cita.
    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que deseas cancelar esta cita? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    Repositorio.cancelarCita(citaId)
                    mostrarConfirmacion = false
                    navController.popBackStack()
                }) {
                    Text("Sí, cancelar", color = RojoTexto)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("No, mantener")
                }
            }
        )
    }
}