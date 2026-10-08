package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.components.BotonPrimario
import com.delacruz.saludpluscitas.ui.components.FilaDato
import com.delacruz.saludpluscitas.ui.components.ResumenMedico
import com.delacruz.saludpluscitas.ui.components.formatearFecha
import com.delacruz.saludpluscitas.ui.components.rangoHora
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.BordeSuave
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

@Composable
fun ConfirmarCitaScreen(
    navController: NavController,
    medicoId: Int,
    fecha: String,
    hora: String
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    var motivo by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Confirmar cita",
                onAtras = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
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
                medico?.let { ResumenMedico(medico = it, mostrarCmp = true) }

                Spacer(modifier = Modifier.height(12.dp))

                FilaDato(Icons.Default.CalendarMonth, "Fecha", formatearFecha(fecha))
                FilaDato(Icons.Default.Schedule, "Hora", rangoHora(hora))
                FilaDato(Icons.Default.LocalHospital, "Tipo de atención", "Consulta presencial")
                FilaDato(Icons.Default.LocationOn, "Dirección", medico?.direccion ?: "")

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text(
                        text = "Motivo de consulta ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(text = "(opcional)", fontSize = 15.sp, color = TextoSecundario)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    placeholder = { Text("Ej. Consulta de rutina", color = TextoSecundario) },
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = BordeSuave
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                }
            }

            BotonPrimario(
                texto = "Agendar cita",
                onClick = {
                    val cita = Repositorio.agendarCita(medicoId, fecha, hora, motivo)
                    if (cita != null) {
                        // Se borra el flujo de agendamiento: Atrás desde el éxito vuelve al Inicio.
                        navController.navigate(Rutas.CitaExitosa.crearRuta(cita.id)) {
                            popUpTo(Rutas.Home.ruta)
                        }
                    } else {
                        error = "Ese horario ya no está disponible. Vuelve y elige otro."
                    }
                },
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}