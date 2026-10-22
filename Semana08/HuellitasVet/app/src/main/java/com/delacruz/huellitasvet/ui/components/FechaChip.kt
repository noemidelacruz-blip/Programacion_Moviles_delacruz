package com.delacruz.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

// Abreviatura del dia en espanol con la primera letra en mayuscula (Mie, Jue, Vie...)
private fun nombreDia(fecha: LocalDate): String =
    fecha.dayOfWeek
        .getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es-PE"))
        .replace(".", "")
        .replaceFirstChar { it.uppercase() }

// Chip de fecha: dia de la semana arriba y numero abajo. El seleccionado se rellena con el
// color primario del perfil. Con mostrarHoy = true, en vez del dia de la semana dice "Hoy"
@Composable
fun FechaChip(
    fecha: LocalDate,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarHoy: Boolean = false
) {
    val forma = RoundedCornerShape(14.dp)
    val colorFondo =
        if (seleccionada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val colorTexto =
        if (seleccionada) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val colorBorde =
        if (seleccionada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Column(
        modifier = modifier
            .width(56.dp)
            .clip(forma)
            .background(colorFondo)
            .border(width = 1.dp, color = colorBorde, shape = forma)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (mostrarHoy) "Hoy" else nombreDia(fecha),
            style = MaterialTheme.typography.labelSmall,
            color = colorTexto
        )
        Text(
            text = fecha.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )
    }
}