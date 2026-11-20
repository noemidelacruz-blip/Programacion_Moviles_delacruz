package com.delacruz.campusgps.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// Botón de contorno que se usa en la fila de acciones del mapa
@Composable
fun BotonSecundario(
    texto: String,
    habilitado: Boolean,
    resaltado: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = when {
                !habilitado -> MaterialTheme.colorScheme.outlineVariant
                else -> MaterialTheme.colorScheme.primary
            }
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            // Fondo opaco: los botones quedan encima de las olas y así el texto se lee
            containerColor = if (resaltado) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            contentColor = if (resaltado) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    ) {
        Text(texto, maxLines = 1)
    }
}
