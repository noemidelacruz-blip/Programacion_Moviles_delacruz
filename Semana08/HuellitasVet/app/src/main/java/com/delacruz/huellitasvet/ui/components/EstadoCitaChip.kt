package com.delacruz.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.ui.theme.colorDeEstado

// Pastilla con el estado de la cita: ambar (Pendiente), verde (Confirmada),
// gris (Atendida) y rojo (Cancelada). El fondo es el mismo color pero muy suave
@Composable
fun EstadoCitaChip(
    estado: EstadoCita,
    modifier: Modifier = Modifier
) {
    val color = colorDeEstado(estado)
    Box(
        modifier = modifier
            .background(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = estado.etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold,
            softWrap = false,
            maxLines = 1
        )
    }
}