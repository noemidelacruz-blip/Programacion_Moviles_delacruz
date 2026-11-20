package com.delacruz.campusgps.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.campusgps.ui.theme.RosaCampus
import com.delacruz.campusgps.ui.theme.VinoCampus
import com.delacruz.campusgps.ui.theme.VinoOscuro

// Botón principal: píldora con degradado vino, ancho aproximado del 78 % y centrado
@Composable
fun BotonUbicacion(
    habilitado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //aqui se cambia los  colores de ese boton.
    val degradado = if (habilitado) {
        Brush.linearGradient(listOf(RosaCampus, VinoCampus, VinoOscuro))
    } else {
        Brush.linearGradient(listOf(Color(0xFFC9A6B4), Color(0xFF9E7C8A)))
    }

    Row(
        modifier = modifier
            .fillMaxWidth(0.78f)
            .height(52.dp)
            .clip(CircleShape)
            .background(degradado)
            .clickable(enabled = habilitado, onClick = onClick)
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconoEnviar(modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Obtener mi ubicación",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            softWrap = false
        )
    }
}

// Flecha de "enviar" dibujada con Path: el proyecto no trae material-icons
@Composable
private fun IconoEnviar(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    Canvas(modifier) {
        val ancho = size.width
        val alto = size.height
        val flecha = Path().apply {
            moveTo(ancho * 0.04f, alto * 0.50f)
            lineTo(ancho * 0.96f, alto * 0.06f)
            lineTo(ancho * 0.62f, alto * 0.94f)
            lineTo(ancho * 0.48f, alto * 0.56f)
            close()
        }
        drawPath(flecha, color)
    }
}
