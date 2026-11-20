package com.delacruz.centrotareas.ui.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.delacruz.centrotareas.ui.theme.IconBoxTint
import com.delacruz.centrotareas.ui.theme.PurpleHeaderCenter
import com.delacruz.centrotareas.ui.theme.PurpleHeaderEnd
import com.delacruz.centrotareas.ui.theme.PurpleHeaderStart

@Composable
fun OlasHeader(modifier: Modifier = Modifier) {
    val headerBrush = Brush.linearGradient(
        colors = listOf(
            PurpleHeaderStart,
            PurpleHeaderCenter,
            PurpleHeaderEnd
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(headerBrush)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val w = size.width
            val h = size.height

            // Forma grande en la esquina superior derecha
            val pathTopRight = Path().apply {
                moveTo(w * 0.35f, 0f)
                cubicTo(w * 0.55f, h * 0.35f, w * 0.85f, h * 0.15f, w, h * 0.40f)
                lineTo(w, 0f)
                close()
            }
            drawPath(pathTopRight, Color(0x808A66D8))

            // Capa 1: Violeta #7F5BD0
            val wave1 = Path().apply {
                moveTo(0f, h * 0.65f)
                cubicTo(w * 0.25f, h * 0.55f, w * 0.60f, h * 0.78f, w, h * 0.60f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(wave1, Color(0xFF7F5BD0))

            // Capa 2: Morado oscuro #382780
            val wave2 = Path().apply {
                moveTo(0f, h * 0.72f)
                cubicTo(w * 0.35f, h * 0.84f, w * 0.70f, h * 0.64f, w, h * 0.68f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(wave2, Color(0xFF382780))

            // Capa 3: Lavanda #B58FEE
            val wave3 = Path().apply {
                moveTo(0f, h * 0.77f)
                cubicTo(w * 0.20f, h * 0.68f, w * 0.55f, h * 0.87f, w, h * 0.73f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(wave3, Color(0xFFB58FEE))

            // Capa 4: Lavanda #C9A0F5 (60 % opacidad)
            val wave4 = Path().apply {
                moveTo(0f, h * 0.81f)
                cubicTo(w * 0.40f, h * 0.89f, w * 0.75f, h * 0.72f, w, h * 0.78f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(wave4, Color(0x99C9A0F5))

            // Capa 5: Lavanda claro #DFBBFD
            val wave5 = Path().apply {
                moveTo(0f, h * 0.85f)
                cubicTo(w * 0.30f, h * 0.79f, w * 0.65f, h * 0.91f, w, h * 0.82f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(wave5, Color(0xFFDFBBFD))

            // Cinta 1: Blanca 70 % opacidad
            val ribbon1 = Path().apply {
                moveTo(0f, h * 0.87f)
                cubicTo(w * 0.35f, h * 0.82f, w * 0.70f, h * 0.93f, w, h * 0.84f)
                cubicTo(w * 0.70f, h * 0.96f, w * 0.35f, h * 0.85f, 0f, h * 0.89f)
                close()
            }
            drawPath(ribbon1, Color(0xB2FFFFFF))

            // Cinta 2: #EBDDFF
            val ribbon2 = Path().apply {
                moveTo(0f, h * 0.90f)
                cubicTo(w * 0.40f, h * 0.85f, w * 0.75f, h * 0.94f, w, h * 0.87f)
                cubicTo(w * 0.75f, h * 0.96f, w * 0.40f, h * 0.88f, 0f, h * 0.92f)
                close()
            }
            drawPath(ribbon2, Color(0xFFEBDDFF))

            // Borde ondulado inferior #E9DFFA
            val waveEdge = Path().apply {
                moveTo(0f, h * 0.93f)
                cubicTo(w * 0.35f, h * 0.88f, w * 0.65f, h * 0.96f, w, h * 0.91f)
                lineTo(w, h + 1f)
                lineTo(0f, h + 1f)
                close()
            }
            drawPath(waveEdge, Color(0xFFE9DFFA))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Logo",
                            tint = IconBoxTint,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "CentroTareas",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Organiza · Procesa · Aprende",
                        color = Color(0xFFE3D0FC),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun OlasFooter(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        val w = size.width
        val h = size.height

        // Capa 1: #E3D0FC
        val f1 = Path().apply {
            moveTo(0f, h * 0.40f)
            cubicTo(w * 0.35f, h * 0.15f, w * 0.70f, h * 0.60f, w, h * 0.10f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(f1, Color(0xFFE3D0FC))

        // Capa 2: #C9A0F5
        val f2 = Path().apply {
            moveTo(0f, h * 0.50f)
            cubicTo(w * 0.30f, h * 0.70f, w * 0.65f, h * 0.25f, w, h * 0.30f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(f2, Color(0xFFC9A0F5))

        // Capa 3: #9B62DC
        val f3 = Path().apply {
            moveTo(0f, h * 0.30f)
            cubicTo(w * 0.25f, h * 0.10f, w * 0.60f, h * 0.65f, w, h * 0.50f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(f3, Color(0xFF9B62DC))

        // Capa 4: #6A38B8 (Colina de morado oscuro más alta a la izquierda)
        val f4 = Path().apply {
            moveTo(0f, h * 0.12f)
            cubicTo(w * 0.30f, h * 0.30f, w * 0.60f, h * 0.80f, w, h * 0.65f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(f4, Color(0xFF6A38B8))

        // Capa 5: Franja delgada #4B2A9A
        val f5 = Path().apply {
            moveTo(0f, h * 0.65f)
            cubicTo(w * 0.40f, h * 0.50f, w * 0.75f, h * 0.88f, w, h * 0.75f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(f5, Color(0xFF4B2A9A))
    }
}
