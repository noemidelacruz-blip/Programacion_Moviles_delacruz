package com.delacruz.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.ui.components.VetBotonTipo
import com.delacruz.huellitasvet.ui.components.VetButton

// Pantalla provisional a la que llega el login. Se reemplaza por Inicio (commit 8)
// y por Panel (commit 12). Sirve para comprobar el login y poder cerrar sesion
@Composable
fun PantallaTemporal(
    titulo: String,
    saludo: String,
    onCerrarSesion: () -> Unit,
    accionExtra: (@Composable () -> Unit)? = null
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = saludo, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Pantalla en construcción",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (accionExtra != null) accionExtra()
            VetButton(
                texto = "Cerrar sesión",
                onClick = onCerrarSesion,
                tipo = VetBotonTipo.CONTORNO,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}