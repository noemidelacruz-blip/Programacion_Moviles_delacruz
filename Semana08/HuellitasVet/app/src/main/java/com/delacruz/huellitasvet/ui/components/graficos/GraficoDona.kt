package com.delacruz.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Grafico de dona con el total en el centro y la leyenda a la derecha.
// Cada dato ocupa un arco proporcional a su valor
@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    tamano: Dp = 110.dp,
    grosor: Dp = 18.dp
) {
    val total = datos.sumOf { it.valor }
    val colorVacio = MaterialTheme.colorScheme.outline

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Dona con el total al centro
        Box(modifier = Modifier.size(tamano), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val trazo = grosor.toPx()
                val lado = size.minDimension - trazo
                val esquina = Offset(trazo / 2f, trazo / 2f)
                val area = Size(lado, lado)

                if (total == 0) {
                    // Sin datos: anillo gris completo
                    drawArc(
                        color = colorVacio,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = esquina,
                        size = area,
                        style = Stroke(width = trazo)
                    )
                } else {
                    // Los arcos empiezan arriba (-90 grados) y avanzan en sentido horario
                    var inicio = -90f
                    datos.forEach { dato ->
                        val barrido = 360f * dato.valor / total
                        if (dato.valor > 0) {
                            drawArc(
                                color = dato.color,
                                startAngle = inicio,
                                sweepAngle = barrido,
                                useCenter = false,
                                topLeft = esquina,
                                size = area,
                                style = Stroke(width = trazo)
                            )
                        }
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = total.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "citas",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Leyenda: cuadrito de color, nombre y cantidad
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            datos.forEach { dato ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(color = dato.color, shape = RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dato.etiqueta,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = dato.valor.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}