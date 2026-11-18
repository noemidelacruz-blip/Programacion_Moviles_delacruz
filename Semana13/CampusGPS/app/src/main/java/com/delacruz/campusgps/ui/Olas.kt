package com.delacruz.campusgps.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delacruz.campusgps.ui.theme.CintaRosa1
import com.delacruz.campusgps.ui.theme.CintaRosa2
import com.delacruz.campusgps.ui.theme.CintaVino1
import com.delacruz.campusgps.ui.theme.CintaVino2
import com.delacruz.campusgps.ui.theme.OlaBaja1
import com.delacruz.campusgps.ui.theme.OlaBaja2
import com.delacruz.campusgps.ui.theme.OlaBaja3
import com.delacruz.campusgps.ui.theme.OlaBaja4
import com.delacruz.campusgps.ui.theme.OlaBaja5
import com.delacruz.campusgps.ui.theme.OlaBaja6
import com.delacruz.campusgps.ui.theme.OlaBaja7
import com.delacruz.campusgps.ui.theme.OlaBaja8
import com.delacruz.campusgps.ui.theme.OlaOscura1
import com.delacruz.campusgps.ui.theme.OlaOscura2
import com.delacruz.campusgps.ui.theme.OlaOscura3
import com.delacruz.campusgps.ui.theme.OlaOscura4
import com.delacruz.campusgps.ui.theme.OlaOscura5
import com.delacruz.campusgps.ui.theme.OlaOscura6
import com.delacruz.campusgps.ui.theme.OlaOscura7
import com.delacruz.campusgps.ui.theme.OlaOscura8
import com.delacruz.campusgps.ui.theme.OlaRosa16
import com.delacruz.campusgps.ui.theme.OlaRosa28
import com.delacruz.campusgps.ui.theme.OlaVinoFondo1
import com.delacruz.campusgps.ui.theme.OlaVinoFondo2
import com.delacruz.campusgps.ui.theme.OlaVinoFondo3
import com.delacruz.campusgps.ui.theme.OlaVinoFondo4

// Cabecera decorativa: degradado vino con borde ondulado, olas translúcidas y dos cintas.
// Todo se dibuja con Canvas y Path, sin imágenes ni dependencias extra.
@Composable
fun OlasSuperiores(
    modifier: Modifier = Modifier,
    altura: Dp = 110.dp,
    content: @Composable BoxScope.() -> Unit = {}
) {
    // La cabecera se dibuja también detrás de la barra de estado
    val insetSuperior = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(altura + insetSuperior)
    ) {
        Canvas(Modifier.fillMaxSize()) {
            dibujarOlasSuperiores()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = insetSuperior)
        ) {
            content()
        }
    }
}

// Panel inferior decorativo: cinco capas de olas de claro a oscuro.
// En tema oscuro usa tonos vino apagados para no deslumbrar.
@Composable
fun OlasInferiores(
    modifier: Modifier = Modifier,
    altura: Dp = 200.dp,
    oscuro: Boolean = false
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(altura)
    ) {
        dibujarOlasInferiores(oscuro)
    }
}

private fun DrawScope.dibujarOlasSuperiores() {
    val ancho = size.width
    val alto = size.height

    // 1) Fondo con degradado de 4 colores y borde inferior ondulado
    val fondo = Path().apply {
        moveTo(0f, 0f)
        lineTo(ancho, 0f)
        lineTo(ancho, alto * 0.60f)
        cubicTo(
            ancho * 0.82f, alto * 0.96f,
            ancho * 0.62f, alto * 0.50f,
            ancho * 0.43f, alto * 0.72f
        )
        cubicTo(
            ancho * 0.25f, alto * 0.92f,
            ancho * 0.10f, alto * 0.62f,
            0f, alto * 0.82f
        )
        close()
    }
    drawPath(
        path = fondo,
        brush = Brush.linearGradient(
            colors = listOf(OlaVinoFondo1, OlaVinoFondo2, OlaVinoFondo3, OlaVinoFondo4),
            start = Offset(0f, 0f),
            end = Offset(ancho, alto * 0.9f)
        )
    )

    // 2) Tres olas translúcidas encima del degradado
    drawPath(
        path = olaRellena(alto * 0.56f, alto * 0.10f, ancho, alto),
        color = OlaRosa16.copy(alpha = 0.16f)
    )
    drawPath(
        path = olaRellena(alto * 0.68f, alto * 0.09f, ancho, alto),
        color = OlaRosa28.copy(alpha = 0.28f)
    )
    drawPath(
        path = olaRellena(alto * 0.80f, alto * 0.07f, ancho, alto),
        color = Color.White.copy(alpha = 0.14f)
    )

    // 3) Dos cintas finas justo en el borde
    drawPath(
        path = lineaOnda(alto * 0.78f, alto * 0.09f, ancho),
        brush = Brush.linearGradient(
            colors = listOf(CintaRosa1, CintaRosa2),
            start = Offset(0f, alto * 0.78f),
            end = Offset(ancho, alto * 0.78f)
        ),
        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
    )
    drawPath(
        path = lineaOnda(alto * 0.87f, alto * 0.07f, ancho),
        brush = Brush.linearGradient(
            colors = listOf(CintaVino1, CintaVino2),
            start = Offset(0f, alto * 0.87f),
            end = Offset(ancho, alto * 0.87f)
        ),
        style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
    )
}

private fun DrawScope.dibujarOlasInferiores(oscuro: Boolean) {
    val ancho = size.width
    val alto = size.height

    // Colores de cada capa según el tema
    val capa1 = if (oscuro) OlaOscura1 else OlaBaja1
    val capa2 = if (oscuro) OlaOscura2 else OlaBaja2
    val capa3 = if (oscuro) listOf(OlaOscura3, OlaOscura4) else listOf(OlaBaja3, OlaBaja4)
    val capa4 = if (oscuro) listOf(OlaOscura5, OlaOscura6) else listOf(OlaBaja5, OlaBaja6)
    val capa5 = if (oscuro) listOf(OlaOscura7, OlaOscura8) else listOf(OlaBaja7, OlaBaja8)

    // Capa 1: la más clara, más arriba
    drawPath(
        path = olaRellena(alto * 0.06f, alto * 0.10f, ancho, alto),
        color = capa1
    )
    // Capa 2
    drawPath(
        path = olaRellena(alto * 0.24f, alto * 0.09f, ancho, alto),
        color = capa2
    )
    // Capa 3: degradado suave
    drawPath(
        path = olaRellena(alto * 0.42f, alto * 0.08f, ancho, alto),
        brush = Brush.linearGradient(
            colors = capa3,
            start = Offset(0f, alto * 0.42f),
            end = Offset(ancho, alto)
        )
    )
    // Capa 4: degradado vino medio
    drawPath(
        path = olaRellena(alto * 0.60f, alto * 0.07f, ancho, alto),
        brush = Brush.linearGradient(
            colors = capa4,
            start = Offset(ancho, alto * 0.60f),
            end = Offset(0f, alto)
        )
    )
    // Capa 5: la más oscura, al fondo
    drawPath(
        path = olaRellena(alto * 0.78f, alto * 0.06f, ancho, alto),
        brush = Brush.linearGradient(
            colors = capa5,
            start = Offset(0f, alto * 0.78f),
            end = Offset(ancho, alto)
        )
    )
}

// Ola cerrada hasta el borde inferior: sirve de capa de color.
private fun olaRellena(baseY: Float, amplitud: Float, ancho: Float, alto: Float): Path =
    Path().apply {
        moveTo(0f, baseY)
        cubicTo(
            ancho * 0.20f, baseY - amplitud,
            ancho * 0.36f, baseY + amplitud,
            ancho * 0.55f, baseY
        )
        cubicTo(
            ancho * 0.74f, baseY - amplitud,
            ancho * 0.88f, baseY + amplitud,
            ancho, baseY - amplitud * 0.5f
        )
        lineTo(ancho, alto)
        lineTo(0f, alto)
        close()
    }

// Línea ondulada abierta: sirve para las cintas del borde.
private fun lineaOnda(baseY: Float, amplitud: Float, ancho: Float): Path =
    Path().apply {
        moveTo(0f, baseY)
        cubicTo(
            ancho * 0.20f, baseY - amplitud,
            ancho * 0.36f, baseY + amplitud,
            ancho * 0.55f, baseY
        )
        cubicTo(
            ancho * 0.74f, baseY - amplitud,
            ancho * 0.88f, baseY + amplitud,
            ancho, baseY - amplitud * 0.5f
        )
    }
