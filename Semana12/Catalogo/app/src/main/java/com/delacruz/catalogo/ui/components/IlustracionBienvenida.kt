package com.delacruz.catalogo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.CoralAccent
import com.delacruz.catalogo.ui.theme.CreamSurface
import com.delacruz.catalogo.ui.theme.PeachSoft
import com.delacruz.catalogo.ui.theme.WaveCoral
import com.delacruz.catalogo.ui.theme.WavePeach

/**
 * Ilustración decorativa de la pantalla de bienvenida.
 *
 * Son formas dibujadas con Canvas (labial, frasco y brillos) para no depender
 * de imágenes externas y mantener la app liviana.
 */
@Composable
fun IlustracionBienvenida(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val ancho = size.width
        val alto = size.height
        val centro = Offset(ancho / 2f, alto / 2f)

        // Círculos de fondo.
        drawCircle(
            color = WavePeach.copy(alpha = 0.55f),
            radius = ancho * 0.42f,
            center = centro
        )
        drawCircle(
            color = CreamSurface.copy(alpha = 0.85f),
            radius = ancho * 0.33f,
            center = centro
        )

        // Labial inclinado.
        rotate(degrees = -14f, pivot = centro) {
            val anchoLabial = ancho * 0.12f
            val altoLabial = alto * 0.34f
            val x = ancho * 0.40f
            val y = alto * 0.30f

            drawRoundRect(
                color = Chocolate,
                topLeft = Offset(x, y),
                size = Size(anchoLabial, altoLabial),
                cornerRadius = CornerRadius(anchoLabial / 2f)
            )
            // Tapa coral del labial.
            drawRoundRect(
                color = CoralAccent,
                topLeft = Offset(x, y - alto * 0.13f),
                size = Size(anchoLabial, alto * 0.14f),
                cornerRadius = CornerRadius(anchoLabial / 2f)
            )
            // Brillo del envase.
            drawRoundRect(
                color = CreamSurface.copy(alpha = 0.35f),
                topLeft = Offset(x + anchoLabial * 0.25f, y + altoLabial * 0.12f),
                size = Size(anchoLabial * 0.18f, altoLabial * 0.5f),
                cornerRadius = CornerRadius(anchoLabial)
            )
        }

        // Frasco de crema.
        drawRoundRect(
            color = CreamSurface,
            topLeft = Offset(ancho * 0.55f, alto * 0.50f),
            size = Size(ancho * 0.20f, alto * 0.24f),
            cornerRadius = CornerRadius(ancho * 0.06f)
        )
        drawRoundRect(
            color = Chocolate,
            topLeft = Offset(ancho * 0.57f, alto * 0.46f),
            size = Size(ancho * 0.16f, alto * 0.07f),
            cornerRadius = CornerRadius(ancho * 0.03f)
        )
        drawRoundRect(
            color = PeachSoft,
            topLeft = Offset(ancho * 0.59f, alto * 0.57f),
            size = Size(ancho * 0.12f, alto * 0.07f),
            cornerRadius = CornerRadius(ancho * 0.02f)
        )

        // Brillos y destellos.
        drawCircle(
            color = CoralAccent.copy(alpha = 0.85f),
            radius = ancho * 0.018f,
            center = Offset(ancho * 0.25f, alto * 0.36f)
        )
        drawCircle(
            color = Chocolate.copy(alpha = 0.5f),
            radius = ancho * 0.010f,
            center = Offset(ancho * 0.78f, alto * 0.33f)
        )
        drawCircle(
            color = WaveCoral,
            radius = ancho * 0.014f,
            center = Offset(ancho * 0.30f, alto * 0.70f)
        )
    }
}
