package com.delacruz.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.delacruz.murocampus.model.Post
import com.delacruz.murocampus.ui.theme.AmarilloPastel
import com.delacruz.murocampus.ui.theme.Durazno
import com.delacruz.murocampus.ui.theme.FondoCrema
import com.delacruz.murocampus.ui.theme.FondoError
import com.delacruz.murocampus.ui.theme.FormaBotonAgregar
import com.delacruz.murocampus.ui.theme.FormaPildora
import com.delacruz.murocampus.ui.theme.FormaTarjeta
import com.delacruz.murocampus.ui.theme.MoradoClaro
import com.delacruz.murocampus.ui.theme.MoradoPrincipal
import com.delacruz.murocampus.ui.theme.SuperficieBlanca
import com.delacruz.murocampus.ui.theme.TamanioBotonAgregar
import com.delacruz.murocampus.ui.theme.TextoCuerpo
import com.delacruz.murocampus.ui.theme.TextoDurazno
import com.delacruz.murocampus.ui.theme.TextoError
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Cabecera común: el amarillo sólido va del borde superior (incluso detrás de
 * la barra de estado) hasta una ola, y detrás asoma una segunda ola más clara
 * con alpha 0.55 sobre el fondo crema. Todo se dibuja con Canvas.
 */
@Composable
fun CabeceraMuro(
    modifier: Modifier = Modifier,
    mostrarAtras: Boolean = false,
    onAtras: () -> Unit = {}
) {
    // La cabecera incluye la franja de la barra de estado para que el amarillo
    // llegue al borde superior de la pantalla.
    val recorteArriba = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ALTO_CABECERA + recorteArriba)
            .background(FondoCrema)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val ancho = size.width
            val alto = ALTO_CABECERA.toPx()
            val arriba = size.height - alto

            // Ola con el relleno hacia arriba: el amarillo cubre toda la
            // cabecera y su borde inferior es la ola. Al desplazarla hacia
            // abajo se obtiene la ola clara que queda detrás.
            fun ola(desfase: Float): Path {
                val desp = alto * desfase
                return Path().apply {
                    moveTo(0f, 0f)
                    lineTo(ancho, 0f)
                    lineTo(ancho, arriba + alto * 0.72f + desp)
                    cubicTo(
                        ancho * 0.84f, arriba + alto * 0.64f + desp,
                        ancho * 0.66f, arriba + alto * 0.82f + desp,
                        ancho * 0.50f, arriba + alto * 0.76f + desp
                    )
                    cubicTo(
                        ancho * 0.34f, arriba + alto * 0.70f + desp,
                        ancho * 0.16f, arriba + alto * 0.62f + desp,
                        0f, arriba + alto * 0.72f + desp
                    )
                    close()
                }
            }

            // Primero la ola clara (más baja) y encima la sólida: entre las dos
            // crestas se ve la franja clara.
            drawPath(path = ola(0.12f), color = AmarilloPastel.copy(alpha = 0.55f))
            drawPath(path = ola(0f), color = AmarilloPastel)

            // Tres trazos cortos en abanico, arriba a la derecha.
            val pivote = Offset(ancho - 52.dp.toPx(), arriba + 44.dp.toPx())
            listOf(105.0, 35.0, -15.0).forEach { grados ->
                val radianes = grados * PI / 180.0
                val desde = 8.dp.toPx()
                val hasta = 34.dp.toPx()
                fun punto(distancia: Float) = Offset(
                    pivote.x + (cos(radianes) * distancia).toFloat(),
                    pivote.y - (sin(radianes) * distancia).toFloat()
                )
                drawLine(
                    color = MoradoPrincipal,
                    start = punto(desde),
                    end = punto(hasta),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(
                    start = if (mostrarAtras) 8.dp else 20.dp,
                    end = 20.dp,
                    top = 38.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mostrarAtras) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onAtras() },
                    contentAlignment = Alignment.Center
                ) {
                    IconoAtras(color = MoradoPrincipal)
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = "MuroCampus",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MoradoPrincipal
            )
        }
    }
}

/** Alto de la cabecera sin contar la franja de la barra de estado. */
private val ALTO_CABECERA = 150.dp

/** Botón + : cuadrado morado de 62 dp con esquinas de 22 dp. */
@Composable
fun BotonAgregar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(TamanioBotonAgregar)
            .clip(FormaBotonAgregar)
            .background(MoradoPrincipal)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(24.dp)) {
            val grosor = 3.dp.toPx()
            drawLine(
                color = Color.White,
                start = Offset(size.width * 0.5f, 0f),
                end = Offset(size.width * 0.5f, size.height),
                strokeWidth = grosor,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White,
                start = Offset(0f, size.height * 0.5f),
                end = Offset(size.width, size.height * 0.5f),
                strokeWidth = grosor,
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * Tarjeta de una publicación: etiqueta con el usuario, título en morado a dos
 * líneas y las dos primeras líneas del texto.
 */
@Composable
fun TarjetaPost(
    post: Post,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = FormaTarjeta,
        colors = CardDefaults.cardColors(containerColor = SuperficieBlanca),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(width = 1.dp, color = MoradoClaro)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            EtiquetaUsuario(userId = post.userId)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MoradoPrincipal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium,
                color = TextoCuerpo,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Etiqueta "Usuario N": morada para los impares y durazno para los pares. */
@Composable
private fun EtiquetaUsuario(userId: Int) {
    val esPar = userId % 2 == 0
    Box(
        modifier = Modifier
            .background(
                color = if (esPar) Durazno else MoradoClaro,
                shape = FormaPildora
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = "Usuario $userId",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = if (esPar) TextoDurazno else MoradoPrincipal
        )
    }
}

/** Indicador de carga con el texto del laboratorio. */
@Composable
fun Cargando(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = FormaTarjeta,
            color = SuperficieBlanca,
            border = BorderStroke(width = 1.dp, color = MoradoClaro)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MoradoPrincipal,
                    strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Cargando publicaciones…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoCuerpo
                )
            }
        }
    }
}

/**
 * Recuadro de error: ilustración de wifi tachado (Canvas), recuadro rosa con
 * el mensaje y botón Reintentar con ícono de recargar.
 */
@Composable
fun ErrorConReintento(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IlustracionWifiSinSenal()

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = FormaTarjeta,
            color = FondoError
        ) {
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextoError,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onReintentar,
            shape = FormaPildora,
            colors = ButtonDefaults.buttonColors(
                containerColor = MoradoPrincipal,
                contentColor = Color.White
            )
        ) {
            IconoRecargar(color = Color.White, tamano = 18.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Reintentar",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** Wifi tachado: tres arcos y un punto morados, con una franja crema y la raya. */
@Composable
private fun IlustracionWifiSinSenal(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(140.dp)) {
        val ancho = size.width
        val alto = size.height
        val centro = Offset(ancho * 0.5f, alto * 0.80f)

        listOf(0.16f, 0.29f, 0.42f).forEach { factor ->
            val radio = ancho * factor
            drawArc(
                color = MoradoPrincipal,
                startAngle = 205f,
                sweepAngle = 130f,
                useCenter = false,
                topLeft = Offset(centro.x - radio, centro.y - radio),
                size = Size(radio * 2f, radio * 2f),
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        drawCircle(
            color = MoradoPrincipal,
            radius = 5.dp.toPx(),
            center = centro
        )

        val inicio = Offset(ancho * 0.14f, alto * 0.12f)
        val fin = Offset(ancho * 0.86f, alto * 0.90f)

        // La franja crema "corta" los arcos y encima queda la raya morada.
        drawLine(
            color = FondoCrema,
            start = inicio,
            end = fin,
            strokeWidth = 16.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = MoradoPrincipal,
            start = inicio,
            end = fin,
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

/** Flecha de atrás dibujada con trazos. */
@Composable
fun IconoAtras(
    color: Color,
    modifier: Modifier = Modifier,
    tamano: Dp = 24.dp
) {
    Canvas(modifier = modifier.size(tamano)) {
        val ancho = size.width
        val alto = size.height
        val grosor = 2.4.dp.toPx()

        drawLine(
            color = color,
            start = Offset(ancho * 0.62f, alto * 0.16f),
            end = Offset(ancho * 0.28f, alto * 0.5f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(ancho * 0.28f, alto * 0.5f),
            end = Offset(ancho * 0.62f, alto * 0.84f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(ancho * 0.30f, alto * 0.5f),
            end = Offset(ancho * 0.86f, alto * 0.5f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}

/** Avión de papel del botón Publicar. */
@Composable
fun IconoEnviar(
    color: Color,
    modifier: Modifier = Modifier,
    tamano: Dp = 18.dp
) {
    Canvas(modifier = modifier.size(tamano)) {
        val ancho = size.width
        val alto = size.height
        val avion = Path().apply {
            moveTo(ancho * 0.06f, alto * 0.08f)
            lineTo(ancho * 0.94f, alto * 0.5f)
            lineTo(ancho * 0.06f, alto * 0.92f)
            lineTo(ancho * 0.06f, alto * 0.62f)
            lineTo(ancho * 0.58f, alto * 0.5f)
            lineTo(ancho * 0.06f, alto * 0.38f)
            close()
        }
        drawPath(path = avion, color = color)
    }
}

/** Flecha circular del botón Reintentar. */
@Composable
fun IconoRecargar(
    color: Color,
    modifier: Modifier = Modifier,
    tamano: Dp = 18.dp
) {
    Canvas(modifier = modifier.size(tamano)) {
        val ancho = size.width
        val alto = size.height

        drawArc(
            color = color,
            startAngle = -60f,
            sweepAngle = 280f,
            useCenter = false,
            topLeft = Offset(ancho * 0.16f, alto * 0.16f),
            size = Size(ancho * 0.68f, alto * 0.68f),
            style = Stroke(width = ancho * 0.12f, cap = StrokeCap.Round)
        )

        val punta = Path().apply {
            moveTo(ancho * 0.74f, alto * 0.04f)
            lineTo(ancho * 0.96f, alto * 0.22f)
            lineTo(ancho * 0.66f, alto * 0.30f)
            close()
        }
        drawPath(path = punta, color = color)
    }
}

/** Burbuja de diálogo junto al título de comentarios. */
@Composable
fun IconoBurbuja(
    color: Color,
    modifier: Modifier = Modifier,
    tamano: Dp = 20.dp
) {
    Canvas(modifier = modifier.size(tamano)) {
        val ancho = size.width
        val alto = size.height
        val burbuja = Path().apply {
            addRoundRect(
                RoundRect(
                    Rect(0f, 0f, ancho, alto * 0.78f),
                    CornerRadius(ancho * 0.28f)
                )
            )
            moveTo(ancho * 0.28f, alto * 0.70f)
            lineTo(ancho * 0.26f, alto * 0.98f)
            lineTo(ancho * 0.54f, alto * 0.76f)
            close()
        }
        drawPath(path = burbuja, color = color)
    }
}
