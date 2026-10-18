package com.delacruz.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.model.Especie

// Colores de pelaje posibles. El color se elige segun el nombre de la mascota,
// asi cada mascota siempre se ve igual y no hace falta guardarlo en la base de datos
private val PelajesPerro = listOf(Color(0xFFD9A066), Color(0xFF8B8E94))
private val PelajesGato = listOf(Color(0xFF9CA3AF), Color(0xFFF59E0B))
private val PelajeOtro = Color(0xFFE9B872)
private val ColorOjos = Color(0xFF1F2937)
private val ColorNariz = Color(0xFF3B2F2F)
private val ColorHocico = Color(0xFFFFF1DE)
private val ColorNarizGato = Color(0xFFF472B6)

// Elige el pelaje segun la especie y el nombre
private fun elegirPelaje(especie: Especie, nombre: String): Color {
    val indice = nombre.hashCode() and Int.MAX_VALUE
    return when (especie) {
        Especie.PERRO -> PelajesPerro[indice % PelajesPerro.size]
        Especie.GATO -> PelajesGato[indice % PelajesGato.size]
        Especie.OTRO -> PelajeOtro
    }
}

// Carita de la mascota dibujada con Canvas: perro (orejas caidas), gato (orejas
// triangulares y bigotes) u otro animal (huella de pata)
@Composable
fun MascotaAvatar(
    especie: Especie,
    modifier: Modifier = Modifier,
    tamano: Dp = 56.dp,
    nombre: String = "",
    fondo: Color = MaterialTheme.colorScheme.primaryContainer
) {
    val pelaje = elegirPelaje(especie, nombre)
    Canvas(modifier = modifier.size(tamano)) {
        // Circulo de fondo
        drawCircle(color = fondo)
        when (especie) {
            Especie.PERRO -> dibujarPerro(pelaje)
            Especie.GATO -> dibujarGato(pelaje)
            Especie.OTRO -> dibujarPata(pelaje)
        }
    }
}

// Cara de perro: cabeza redonda, orejas caidas, hocico claro, nariz y ojos
private fun DrawScope.dibujarPerro(pelaje: Color) {
    val w = size.width
    val h = size.height
    val oreja = lerp(pelaje, Color.Black, 0.3f)

    // Cabeza
    drawCircle(color = pelaje, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.52f))
    // Orejas caidas a los lados
    drawOval(color = oreja, topLeft = Offset(w * 0.17f, h * 0.24f), size = Size(w * 0.2f, h * 0.42f))
    drawOval(color = oreja, topLeft = Offset(w * 0.63f, h * 0.24f), size = Size(w * 0.2f, h * 0.42f))
    // Hocico y nariz
    drawOval(color = ColorHocico, topLeft = Offset(w * 0.37f, h * 0.54f), size = Size(w * 0.26f, h * 0.2f))
    drawOval(color = ColorNariz, topLeft = Offset(w * 0.45f, h * 0.56f), size = Size(w * 0.1f, h * 0.07f))
    // Ojos
    drawCircle(color = ColorOjos, radius = w * 0.035f, center = Offset(w * 0.4f, h * 0.46f))
    drawCircle(color = ColorOjos, radius = w * 0.035f, center = Offset(w * 0.6f, h * 0.46f))
}

// Cara de gato: orejas triangulares, ojos, nariz rosada y bigotes
private fun DrawScope.dibujarGato(pelaje: Color) {
    val w = size.width
    val h = size.height

    // Orejas triangulares
    val orejaIzquierda = Path().apply {
        moveTo(w * 0.22f, h * 0.20f)
        lineTo(w * 0.44f, h * 0.36f)
        lineTo(w * 0.20f, h * 0.50f)
        close()
    }
    val orejaDerecha = Path().apply {
        moveTo(w * 0.78f, h * 0.20f)
        lineTo(w * 0.56f, h * 0.36f)
        lineTo(w * 0.80f, h * 0.50f)
        close()
    }
    drawPath(path = orejaIzquierda, color = pelaje)
    drawPath(path = orejaDerecha, color = pelaje)

    // Cabeza
    drawCircle(color = pelaje, radius = w * 0.29f, center = Offset(w * 0.5f, h * 0.56f))
    // Ojos y nariz
    drawCircle(color = ColorOjos, radius = w * 0.035f, center = Offset(w * 0.4f, h * 0.52f))
    drawCircle(color = ColorOjos, radius = w * 0.035f, center = Offset(w * 0.6f, h * 0.52f))
    drawCircle(color = ColorNarizGato, radius = w * 0.03f, center = Offset(w * 0.5f, h * 0.6f))

    // Bigotes (dos por lado)
    val bigote = ColorOjos.copy(alpha = 0.6f)
    val grosor = w * 0.012f
    drawLine(color = bigote, start = Offset(w * 0.32f, h * 0.62f), end = Offset(w * 0.15f, h * 0.59f), strokeWidth = grosor)
    drawLine(color = bigote, start = Offset(w * 0.32f, h * 0.66f), end = Offset(w * 0.15f, h * 0.70f), strokeWidth = grosor)
    drawLine(color = bigote, start = Offset(w * 0.68f, h * 0.62f), end = Offset(w * 0.85f, h * 0.59f), strokeWidth = grosor)
    drawLine(color = bigote, start = Offset(w * 0.68f, h * 0.66f), end = Offset(w * 0.85f, h * 0.70f), strokeWidth = grosor)
}

// Otro animal: huella de pata (almohadilla grande y cuatro dedos)
private fun DrawScope.dibujarPata(pelaje: Color) {
    val w = size.width
    val h = size.height

    drawOval(color = pelaje, topLeft = Offset(w * 0.3f, h * 0.48f), size = Size(w * 0.4f, h * 0.3f))
    drawCircle(color = pelaje, radius = w * 0.07f, center = Offset(w * 0.27f, h * 0.42f))
    drawCircle(color = pelaje, radius = w * 0.07f, center = Offset(w * 0.41f, h * 0.3f))
    drawCircle(color = pelaje, radius = w * 0.07f, center = Offset(w * 0.59f, h * 0.3f))
    drawCircle(color = pelaje, radius = w * 0.07f, center = Offset(w * 0.73f, h * 0.42f))
}