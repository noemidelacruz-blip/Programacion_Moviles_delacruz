package com.delacruz.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
 * Composable que representa la pantalla de confirmacion tras agendar una cita.
 *
 * @param doctorNombre Nombre del medico seleccionado.
 * @param fecha Fecha elegida para la cita.
 * @param hora Hora elegida para la cita.
 * @param onVerMisCitasClick Callback para navegar a la pantalla de "Mis citas".
 */
@Composable
fun ConfirmationScreen(
    doctorNombre: String,
    fecha: String,
    hora: String,
    onVerMisCitasClick: () -> Unit
) {
    // Contenedor principal centrado vertical y horizontalmente
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono de confirmacion verde dentro de un circulo con fondo claro
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)), // Fondo verde suave
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check, // Icono de check
                contentDescription = "Confirmado",
                modifier = Modifier.size(50.dp),
                tint = Color(0xFF2E7D32) // Verde oscuro
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Titulo principal de confirmacion
        Text(
            text = "¡Cita agendada!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Nombre del doctor asignado a la cita
        Text(
            text = doctorNombre,
            fontSize = 16.sp,
            color = Color.Gray
        )

        // Resumen de la fecha y hora seleccionadas
        Text(
            text = "$fecha, $hora",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Boton para ir directamente a la pantalla de "Mis citas"
        Button(
            onClick = onVerMisCitasClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFEFEF))
        ) {
            Text(
                text = "Ver mis citas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}