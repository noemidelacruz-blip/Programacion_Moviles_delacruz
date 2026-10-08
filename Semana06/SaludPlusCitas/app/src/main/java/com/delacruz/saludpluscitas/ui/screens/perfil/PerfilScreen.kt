package com.delacruz.saludpluscitas.ui.screens.perfil

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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.AvatarMedico
import com.delacruz.saludpluscitas.ui.components.BarraNavegacion
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.components.FilaDato
import com.delacruz.saludpluscitas.ui.theme.BordeSuave
import com.delacruz.saludpluscitas.ui.theme.RojoTexto
import com.delacruz.saludpluscitas.ui.theme.SuperficieBlanca
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

// Vista sin diseño de referencia: sigue el estilo de Confirmar cita.
@Composable
fun PerfilScreen(
    navController: NavController
) {
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Mis datos",
                onAtras = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BarraNavegacion(
                navController = navController,
                rutaActual = Rutas.Perfil.ruta
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarMedico(tamano = 96)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = usuario?.nombre ?: "",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(text = "Paciente", fontSize = 14.sp, color = TextoSecundario)

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SuperficieBlanca),
                border = BorderStroke(1.dp, BordeSuave),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    FilaDato(Icons.Default.Phone, "Teléfono", usuario?.telefono ?: "")
                    FilaDato(
                        Icons.Default.Email,
                        "Correo",
                        usuario?.correo?.ifBlank { "No registrado" } ?: ""
                    )
                    FilaDato(
                        Icons.Default.EventAvailable,
                        "Citas agendadas",
                        if (totalCitas == 1) "1 cita" else "$totalCitas citas"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = {
                    Repositorio.cerrarSesion()
                    // Se limpia todo el historial: Atrás no puede volver a la sesión.
                    navController.navigate(Rutas.Splash.ruta) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, RojoTexto),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = RojoTexto
                )
                Text(
                    text = "  Cerrar sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RojoTexto
                )
            }
        }
    }
}