package com.delacruz.catalogo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import com.delacruz.catalogo.ui.theme.PeachGradientBottom
import com.delacruz.catalogo.ui.theme.PeachGradientTop
import com.delacruz.catalogo.ui.theme.WaveCoral
import com.delacruz.catalogo.ui.theme.WavePeach

/**
 * Fondo común de la aplicación.
 *
 * Dibuja el degradado vertical durazno de las capturas y, encima, ondas
 * orgánicas y manchas suaves con Canvas. No usa imágenes externas: las ondas
 * son vectores para que se vean nítidas en cualquier tamaño de pantalla.
 */
@Composable
fun FondoBeautyEmi(
    modifier: Modifier = Modifier,
    contenido: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PeachGradientTop, PeachGradientBottom)
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val ancho = size.width
            val alto = size.height

            // Manchas suaves: una detrás del encabezado y otra al costado.
            drawCircle(
                color = WavePeach.copy(alpha = 0.30f),
                radius = ancho * 0.55f,
                center = Offset(ancho * 1.02f, alto * 0.06f)
            )
            drawCircle(
                color = WaveCoral.copy(alpha = 0.14f),
                radius = ancho * 0.34f,
                center = Offset(ancho * -0.10f, alto * 0.30f)
            )

            // Onda inferior clara.
            val ondaClara = Path().apply {
                moveTo(0f, alto * 0.80f)
                cubicTo(
                    ancho * 0.22f, alto * 0.74f,
                    ancho * 0.58f, alto * 0.90f,
                    ancho, alto * 0.79f
                )
                lineTo(ancho, alto)
                lineTo(0f, alto)
                close()
            }
            drawPath(ondaClara, color = WavePeach.copy(alpha = 0.45f))

            // Onda inferior coral.
            val ondaCoral = Path().apply {
                moveTo(0f, alto * 0.90f)
                cubicTo(
                    ancho * 0.30f, alto * 0.84f,
                    ancho * 0.72f, alto * 0.98f,
                    ancho, alto * 0.87f
                )
                lineTo(ancho, alto)
                lineTo(0f, alto)
                close()
            }
            drawPath(ondaCoral, color = WaveCoral.copy(alpha = 0.40f))
        }

        contenido()
    }
}
