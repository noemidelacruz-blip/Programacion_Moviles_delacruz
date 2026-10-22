package com.delacruz.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

// Chip de hora de la pantalla Pedir cita. Tres estados:
// - normal: se puede elegir; seleccionada: relleno con el color primario
// - ocupada: otra cita ya tiene ese horario; sale gris y tachada y no se puede tocar
// - no habilitada: la hora ya paso (si la fecha es hoy); sale apagada y no se puede tocar
@Composable
fun HoraChip(
    hora: String,
    seleccionada: Boolean,
    ocupada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitada: Boolean = true
) {
    val forma = RoundedCornerShape(12.dp)
    val sePuedeTocar = habilitada && !ocupada

    val colorFondo = when {
        ocupada -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        seleccionada -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }
    val colorTexto = when {
        seleccionada -> MaterialTheme.colorScheme.onPrimary
        sePuedeTocar -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    }
    val colorBorde = when {
        seleccionada -> MaterialTheme.colorScheme.primary
        ocupada -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        else -> MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = modifier
            .clip(forma)
            .background(colorFondo)
            .border(width = 1.dp, color = colorBorde, shape = forma)
            .clickable(enabled = sePuedeTocar, onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = hora,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = colorTexto,
            textAlign = TextAlign.Center,
            textDecoration = if (ocupada) TextDecoration.LineThrough else TextDecoration.None
        )
    }
}