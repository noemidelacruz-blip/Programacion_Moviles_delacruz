package com.delacruz.tecsupfit.screens

// Importaciones de Jetpack Compose y Material 3
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.delacruz.tecsupfit.model.PerfilUsuario

/**
 * Pantalla que muestra el perfil del usuario activo y sus opciones de cuenta.
 */
@Composable
fun PantallaPerfil() {
    // Instancia del perfil del usuario adaptada a la data class PerfilUsuario(nombre)
    val usuario = PerfilUsuario(
        nombre = "Diego De La Cruz"
    )

    // Contenedor principal vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título superior alineado a la izquierda
        Text(
            text = "Mi Perfil",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        // Avatar circular para la foto de perfil
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(VerdeClaroBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = VerdeTecsup,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre del usuario obtenido del modelo
        Text(
            text = usuario.nombre,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "diego.delacruz@tecsup.edu.pe",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tarjeta con lista de opciones y configuraciones de cuenta
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GrisTarjeta),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                ItemOpcionPerfil("Historial de asistencias")
                HorizontalDivider(color = Color.White, thickness = 1.dp)
                ItemOpcionPerfil("Notificaciones y recordatorios")
                HorizontalDivider(color = Color.White, thickness = 1.dp)
                ItemOpcionPerfil("Términos y condiciones")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón para cerrar sesión
        OutlinedButton(
            onClick = { /* Acción para cerrar sesión */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
        }
    }
}

/**
 * Composable auxiliar para renderizar cada fila de opción en el menú de perfil.
 */
@Composable
private fun ItemOpcionPerfil(titulo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}