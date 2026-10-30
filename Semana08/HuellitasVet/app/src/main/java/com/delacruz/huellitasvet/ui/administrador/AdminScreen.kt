package com.delacruz.huellitasvet.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.model.Rol
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.AvatarUsuario
import com.delacruz.huellitasvet.ui.components.VetBotonTipo
import com.delacruz.huellitasvet.ui.components.VetButton

// Pantalla principal del administrador: muestra su codigo, nombre, apellidos y correo.
// No atiende citas ni registra mascotas; solo tiene su cuenta y puede cerrar sesion
@Composable
fun AdminScreen(
    usuario: Usuario,
    onCerrarSesion: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar(titulo = "Administración") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AvatarUsuario(rol = Rol.ADMINISTRADOR, tamano = 96.dp)

            // Nombre completo: nombre y apellidos (los apellidos pueden ser nulos)
            Text(
                text = listOfNotNull(usuario.nombre, usuario.apellidos).joinToString(" "),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Chip del perfil
            Text(
                text = "Administrador",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            // Datos de la cuenta
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Dato(etiqueta = "Código", valor = usuario.codigo ?: "Sin código")
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Dato(etiqueta = "Nombre", valor = usuario.nombre)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Dato(etiqueta = "Apellidos", valor = usuario.apellidos ?: "Sin apellidos")
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Dato(etiqueta = "Correo", valor = usuario.correo)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            VetButton(
                texto = "Cerrar sesión",
                onClick = onCerrarSesion,
                tipo = VetBotonTipo.CONTORNO,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

// Una fila de la tarjeta: etiqueta pequena arriba y el valor debajo
@Composable
private fun Dato(etiqueta: String, valor: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
