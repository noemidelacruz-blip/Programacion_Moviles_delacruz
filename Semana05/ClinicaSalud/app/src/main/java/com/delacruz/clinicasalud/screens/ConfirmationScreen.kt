package com.delacruz.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de confirmacion de cita mejorada (Rama con-ia).
 *
 * @param doctorNombre Nombre del medico.
 * @param especialidad Especialidad medica (ej. Cardiologia).
 * @param fecha Fecha agendada.
 * @param hora Hora agendada.
 * @param onVolverAtras Callback para la flecha de la TopBar.
 * @param onVerMisCitasClick Callback para el boton 'Ver mis citas'.
 * @param onVolverInicioClick Callback para el boton 'Volver al inicio'.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    doctorNombre: String = "Dra. Ana Torres",
    especialidad: String = "Cardiología",
    fecha: String = "Sáb 28",
    hora: String = "9:00",
    onVolverAtras: () -> Unit = {},
    onVerMisCitasClick: () -> Unit = {},
    onVolverInicioClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            // Barra superior con titulo 'Confirmacion' y flecha de retorno
            TopAppBar(
                title = { Text("Confirmación", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolverAtras) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Circulo de verificacion con icono verde
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirmado",
                    modifier = Modifier.size(45.dp),
                    tint = Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Titulo y mensaje descriptivo
            Text(
                text = "¡Cita agendada!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Tu cita ha sido registrada correctamente.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta contenedora con el resumen completo de la cita
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F2F8))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Resumen de la cita",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = "Médico: $doctorNombre",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Especialidad: $especialidad",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Fecha: $fecha",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Hora: $hora",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Boton Principal Morado: Ver mis citas
            Button(
                onClick = onVerMisCitasClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF532D8C))
            ) {
                Text(
                    text = "Ver mis citas",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Boton Secundario: Volver al inicio
            OutlinedButton(
                onClick = onVolverInicioClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFECE6F0))
            ) {
                Text(
                    text = "Volver al inicio",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A148C)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}