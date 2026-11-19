package com.delacruz.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Chips de categoría: filtran el mapa y la lista a la vez.
// "Todos" quita el filtro; tocar el chip activo también lo quita.
// Si no caben en una línea, pasan a la siguiente en vez de quedar cortados.
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipsCategoria(
    categorias: List<String>,
    seleccionada: String?,
    onSeleccionar: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ChipCategoria(
            texto = "Todos",
            activo = seleccionada == null,
            onClick = { onSeleccionar(null) }
        )

        categorias.forEach { categoria ->
            ChipCategoria(
                texto = categoria,
                activo = categoria == seleccionada,
                onClick = { onSeleccionar(categoria) }
            )
        }
    }
}

@Composable
private fun ChipCategoria(
    texto: String,
    activo: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (activo) MaterialTheme.colorScheme.primary else Color.Transparent,
        contentColor = if (activo) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        border = if (activo) {
            null
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        }
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
    }
}
