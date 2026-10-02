package com.delacruz.tecsupfit.screens

// Importaciones de Jetpack Compose y Material 3
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla que muestra el mensaje de confirmación de reserva exitosa.
 * @param nombre Nombre de la clase reservada.
 * @param hora Hora de la clase.
 * @param sala Sala asignada.
 * @param onVerReservas Callback para ir a la lista de reservas activas.
 */
@Composable
fun PantallaConfirmacion(
    nombre: String,
    hora: String,
    sala: String,
    onVerReservas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Círculo con el ícono de Check en verde
        Box(
            modifier = Modifier.size(80.dp).background(VerdeClaroBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = VerdeTecsup, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Textos del resumen de la reserva confirmada
        Text("¡Cupo reservado!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(nombre, fontSize = 16.sp, color = Color.Gray)
        Text("Hoy, $hora · $sala", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(40.dp))

        // Botón para redirigir a la pantalla de mis reservas
        Button(
            onClick = onVerReservas,
            colors = ButtonDefaults.buttonColors(containerColor = GrisTarjeta),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Ver mis reservas", color = Color.Black)
        }
    }
}