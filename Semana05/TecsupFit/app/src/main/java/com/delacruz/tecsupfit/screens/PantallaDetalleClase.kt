package com.delacruz.tecsupfit.screens

// Importaciones de Jetpack Compose y Material 3
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.delacruz.tecsupfit.model.ClaseFit

/**
 * Pantalla que muestra el detalle completo de la clase seleccionada.
 * @param clase Objeto con la información de la clase.
 * @param onBack Callback para regresar a la pantalla anterior.
 * @param onReservar Callback para proceder a confirmar la reserva.
 */
@Composable
fun PantallaDetalleClase(
    clase: ClaseFit,
    onBack: () -> Unit,
    onReservar: () -> Unit
) {
    // Contenedor principal vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Barra superior con botón de regresar y título
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
            Text("Detalle de clase", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Banner central ilustrativo con ícono
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(VerdeClaroBg, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = VerdeTecsup,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Título de la clase y subdatos (hora, sala y duración)
        Text(clase.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("${clase.hora} · ${clase.sala} · ${clase.duracion}", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción detallada de la clase
        Text(clase.descripcion, fontSize = 14.sp, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(20.dp))

        // Información de cupos disponibles
        Text("${clase.cuposDisponibles} de ${clase.cuposTotales} cupos disponibles", fontSize = 14.sp, fontWeight = FontWeight.Medium)

        // Empuja el botón hacia el fondo de la pantalla
        Spacer(modifier = Modifier.weight(1f))

        // Botón principal para realizar la reserva
        Button(
            onClick = onReservar,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeTecsup),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Reservar cupo", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}