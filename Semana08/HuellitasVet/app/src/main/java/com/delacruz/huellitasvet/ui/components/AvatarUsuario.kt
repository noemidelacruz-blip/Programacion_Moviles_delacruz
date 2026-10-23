package com.delacruz.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.model.Rol

// Tonos de la ilustracion (piel, cabello, ojos). Son colores propios del dibujo,
// como los pelajes de MascotaAvatar, no colores del tema
private val Piel = Color(0xFFF3C9A6)
private val Cabello = Color(0xFF4A2F23)
private val CabelloOscuro = Color(0xFF34211A)
private val Ojos = Color(0xFF2B2119)
private val Mejilla = Color(0xFFE88E8E)
private val Boca = Color(0xFFB4575E)
private val Estetoscopio = Color(0xFF374151)

// Avatar del usuario dibujado con Canvas, siguiendo el estilo de MascotaAvatar.
// El dueno lleva melena larga con el color del perfil; la veterinaria lleva el
// pelo recogido, bata clara y estetoscopio. El color sale del tema del perfil
@Composable
fun AvatarUsuario(
    rol: Rol,
    modifier: Modifier = Modifier,
    tamano: Dp = 60.dp
) {
    val fondo = MaterialTheme.colorScheme.primaryContainer
    val acento = MaterialTheme.colorScheme.primary
    val bata = MaterialTheme.colorScheme.surface
    Canvas(modifier = modifier.size(tamano).clip(CircleShape)) {
        drawCircle(color = fondo)
        if (rol == Rol.ADMIN) dibujarVeterinaria(acento, bata) else dibujarDueno(acento)
    }
}

// Dueno: melena larga que enmarca la cara y cae sobre los hombros, con blusa del color del perfil
private fun DrawScope.dibujarDueno(acento: Color) {
    val w = size.width
    val h = size.height

    // Melena larga detras de la cabeza
    drawOval(
        color = Cabello,
        topLeft = Offset(w * 0.19f, h * 0.13f),
        size = Size(w * 0.62f, h * 0.78f)
    )
    // Hombrera (blusa) con el color del perfil
    drawRoundRect(
        color = acento,
        topLeft = Offset(w * 0.13f, h * 0.72f),
        size = Size(w * 0.74f, h * 0.46f),
        cornerRadius = CornerRadius(w * 0.35f, w * 0.35f)
    )
    // Mechones delante de los hombros para dar sensacion de pelo largo
    drawRoundRect(
        color = Cabello,
        topLeft = Offset(w * 0.19f, h * 0.48f),
        size = Size(w * 0.11f, h * 0.36f),
        cornerRadius = CornerRadius(w * 0.055f, w * 0.055f)
    )
    drawRoundRect(
        color = Cabello,
        topLeft = Offset(w * 0.70f, h * 0.48f),
        size = Size(w * 0.11f, h * 0.36f),
        cornerRadius = CornerRadius(w * 0.055f, w * 0.055f)
    )
    dibujarCabeza()
}

// Veterinaria: pelo recogido con rodete, bata clara, escote en V y estetoscopio
private fun DrawScope.dibujarVeterinaria(acento: Color, bata: Color) {
    val w = size.width
    val h = size.height

    // Pelo recogido y rodete
    drawOval(
        color = Cabello,
        topLeft = Offset(w * 0.22f, h * 0.15f),
        size = Size(w * 0.56f, h * 0.5f)
    )
    drawCircle(color = Cabello, radius = w * 0.1f, center = Offset(w * 0.5f, h * 0.13f))

    // Bata (cuerpo)
    drawRoundRect(
        color = bata,
        topLeft = Offset(w * 0.13f, h * 0.72f),
        size = Size(w * 0.74f, h * 0.46f),
        cornerRadius = CornerRadius(w * 0.35f, w * 0.35f)
    )
    // Escote en V del uniforme, con el color del perfil
    val escote = Path().apply {
        moveTo(w * 0.42f, h * 0.73f)
        lineTo(w * 0.58f, h * 0.73f)
        lineTo(w * 0.5f, h * 0.9f)
        close()
    }
    drawPath(path = escote, color = acento)

    dibujarCabeza()
    // El estetoscopio va encima de la bata
    dibujarEstetoscopio()
}

// Cuello, cara, flequillo y rasgos. Lo comparten los dos avatares
private fun DrawScope.dibujarCabeza() {
    val w = size.width
    val h = size.height

    // Cuello
    drawRoundRect(
        color = Piel,
        topLeft = Offset(w * 0.44f, h * 0.56f),
        size = Size(w * 0.12f, h * 0.16f),
        cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
    )
    // Cara
    drawCircle(color = Piel, radius = w * 0.2f, center = Offset(w * 0.5f, h * 0.42f))
    // Pelo de la frente
    drawOval(
        color = Cabello,
        topLeft = Offset(w * 0.28f, h * 0.155f),
        size = Size(w * 0.44f, h * 0.19f)
    )
    dibujarRasgos()
}

// Cejas, ojos, mejillas y sonrisa
private fun DrawScope.dibujarRasgos() {
    val w = size.width
    val h = size.height

    // Cejas
    drawLine(CabelloOscuro, Offset(w * 0.40f, h * 0.40f), Offset(w * 0.46f, h * 0.39f), strokeWidth = w * 0.012f)
    drawLine(CabelloOscuro, Offset(w * 0.54f, h * 0.39f), Offset(w * 0.60f, h * 0.40f), strokeWidth = w * 0.012f)
    // Ojos
    drawCircle(color = Ojos, radius = w * 0.022f, center = Offset(w * 0.43f, h * 0.46f))
    drawCircle(color = Ojos, radius = w * 0.022f, center = Offset(w * 0.57f, h * 0.46f))
    // Mejillas
    drawCircle(color = Mejilla.copy(alpha = 0.5f), radius = w * 0.035f, center = Offset(w * 0.37f, h * 0.52f))
    drawCircle(color = Mejilla.copy(alpha = 0.5f), radius = w * 0.035f, center = Offset(w * 0.63f, h * 0.52f))
    // Boca (sonrisa)
    drawArc(
        color = Boca,
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(w * 0.45f, h * 0.50f),
        size = Size(w * 0.10f, h * 0.08f),
        style = Stroke(width = w * 0.014f)
    )
}

// Estetoscopio: tubos en U sobre el pecho y campana al centro
private fun DrawScope.dibujarEstetoscopio() {
    val w = size.width
    val h = size.height
    val grosor = w * 0.022f

    drawArc(
        color = Estetoscopio,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(w * 0.36f, h * 0.62f),
        size = Size(w * 0.28f, h * 0.26f),
        style = Stroke(width = grosor)
    )
    // Los tubos suben hacia el cuello
    drawLine(Estetoscopio, Offset(w * 0.36f, h * 0.75f), Offset(w * 0.40f, h * 0.66f), strokeWidth = grosor)
    drawLine(Estetoscopio, Offset(w * 0.64f, h * 0.75f), Offset(w * 0.60f, h * 0.66f), strokeWidth = grosor)
    // Campana
    drawCircle(color = Estetoscopio, radius = w * 0.04f, center = Offset(w * 0.5f, h * 0.88f))
}
