package com.delacruz.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.centrotareas.model.CargaUi
import com.delacruz.centrotareas.model.PrimosUi
import com.delacruz.centrotareas.ui.componentes.BotonGradient
import com.delacruz.centrotareas.ui.componentes.BotonOutlinedCustom
import com.delacruz.centrotareas.ui.componentes.EncabezadoTarjeta
import com.delacruz.centrotareas.ui.componentes.OlasFooter
import com.delacruz.centrotareas.ui.componentes.OlasHeader
import com.delacruz.centrotareas.ui.componentes.TablaComparacionTiempos
import com.delacruz.centrotareas.ui.componentes.TarjetaEstilizada
import com.delacruz.centrotareas.ui.theme.CardBackground
import com.delacruz.centrotareas.ui.theme.CardTitleColor
import com.delacruz.centrotareas.ui.theme.IconBoxBg
import com.delacruz.centrotareas.ui.theme.IconBoxTint
import com.delacruz.centrotareas.ui.theme.OutlinedBtnBorder
import com.delacruz.centrotareas.ui.theme.OutlinedBtnText
import com.delacruz.centrotareas.ui.theme.ProgressFill
import com.delacruz.centrotareas.ui.theme.ProgressTrack
import com.delacruz.centrotareas.ui.theme.ScreenBgBottom
import com.delacruz.centrotareas.ui.theme.ScreenBgMiddle
import com.delacruz.centrotareas.ui.theme.ScreenBgTop
import com.delacruz.centrotareas.ui.theme.SuccessGreenBg
import com.delacruz.centrotareas.ui.theme.SuccessGreenText
import com.delacruz.centrotareas.ui.theme.SwitchTrackChecked
import com.delacruz.centrotareas.ui.theme.TextPrimary
import com.delacruz.centrotareas.ui.theme.TextSecondary
import com.delacruz.centrotareas.viewmodel.TareasViewModel

@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    var toques by rememberSaveable { mutableIntStateOf(0) }
    var textoHasta by rememberSaveable { mutableStateOf("500000") }
    var hastaContado by rememberSaveable { mutableIntStateOf(0) }
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val progresoPrimos by viewModel.progresoPrimos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simulandoError by viewModel.simularError.collectAsState()
    val historialTiempos by viewModel.historialTiempos.collectAsState()
    val historial by viewModel.historial.collectAsState()

    val backgroundGradient = Brush.verticalGradient(
        0.0f to ScreenBgTop,
        0.18f to ScreenBgTop,
        0.60f to ScreenBgMiddle,
        1.00f to ScreenBgBottom
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        // Ondas inferiores fijas al fondo
        OlasFooter(
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Encabezado decorativo con ondas superiores
            OlasHeader()

            // Contenido con scroll vertical y padding inferior amplio (~96dp)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarjeta 1: Toques + Tarea con progreso
                TarjetaEstilizada {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = IconBoxBg
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "Toques",
                                    color = TextSecondary,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = "$toques",
                                    color = CardTitleColor,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        BotonGradient(
                            onClick = { toques++ },
                            text = "Tócame",
                            icon = Icons.Filled.PlayArrow
                        )
                    }
                }

                TarjetaEstilizada {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = CircleShape,
                        color = IconBoxBg
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                start = 4.dp,
                                end = 16.dp,
                                top = 4.dp,
                                bottom = 4.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(36.dp),
                                shape = RoundedCornerShape(50),
                                color = CardBackground
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.RocketLaunch,
                                        contentDescription = null,
                                        tint = IconBoxTint,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Tarea con progreso",
                                color = CardTitleColor,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.tareaLarga() },
                            enabled = !corriendo,
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IconBoxBg,
                                contentColor = OutlinedBtnText,
                                disabledContainerColor = IconBoxBg.copy(alpha = 0.5f),
                                disabledContentColor = OutlinedBtnText.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Iniciar", fontWeight = FontWeight.SemiBold)
                        }

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(1.dp)
                                .height(32.dp)
                                .background(OutlinedBtnBorder)
                        )

                        OutlinedButton(
                            onClick = { viewModel.cancelar() },
                            enabled = corriendo,
                            shape = CircleShape,
                            border = BorderStroke(
                                1.5.dp,
                                if (corriendo) OutlinedBtnBorder else OutlinedBtnBorder.copy(alpha = 0.4f)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = IconBoxBg,
                                contentColor = OutlinedBtnText,
                                disabledContainerColor = IconBoxBg.copy(alpha = 0.5f),
                                disabledContentColor = OutlinedBtnText.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Cancelar", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        color = ProgressFill,
                        trackColor = ProgressTrack
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${(progreso * 100).toInt()} %",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (mensaje == "Terminó") {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreenText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = mensaje,
                                color = if (mensaje == "Terminó") SuccessGreenText else TextSecondary,
                                fontWeight = if (mensaje == "Terminó") FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Tarjeta 2: Contar números primos
                TarjetaEstilizada {
                    EncabezadoTarjeta(
                        icon = Icons.Filled.Calculate,
                        titulo = "Contar números primos"
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = textoHasta,
                            onValueChange = { entrada ->
                                textoHasta = entrada.filter { it.isDigit() }
                            },
                            label = { Text("Hasta el número") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OutlinedBtnText,
                                unfocusedBorderColor = OutlinedBtnBorder,
                                focusedLabelColor = OutlinedBtnText,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BotonGradient(
                                onClick = {
                                    val hasta = textoHasta.toIntOrNull()
                                    if (hasta != null && hasta >= 2) {
                                        hastaContado = hasta
                                        viewModel.contar(hasta)
                                    }
                                },
                                enabled = primos !is PrimosUi.Calculando,
                                text = "Contar",
                                icon = Icons.Filled.Search
                            )
                            BotonOutlinedCustom(
                                onClick = { viewModel.cancelarPrimos() },
                                enabled = primos is PrimosUi.Calculando,
                                text = "Cancelar",
                                icon = Icons.Filled.Close
                            )
                        }

                        val estadoPrimos = primos
                        if (estadoPrimos is PrimosUi.Listo) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = SuccessGreenBg
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Primos hasta $hastaContado: ${estadoPrimos.cantidad}",
                                        color = SuccessGreenText,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Tiempo: ${estadoPrimos.milisegundos} ms",
                                        color = SuccessGreenText
                                    )
                                }
                            }
                        } else if (estadoPrimos is PrimosUi.Calculando) {
                            LinearProgressIndicator(
                                progress = { progresoPrimos / 100f },
                                modifier = Modifier.fillMaxWidth(),
                                color = ProgressFill,
                                trackColor = ProgressTrack
                            )
                            Text(
                                text = "Calculando... $progresoPrimos %",
                                color = TextPrimary
                            )
                        } else {
                            Text(
                                text = when (estadoPrimos) {
                                    PrimosUi.Inicial -> "Ingresa un número mayor o igual a 2"
                                    PrimosUi.Cancelado -> "Cancelado"
                                    else -> ""
                                },
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Tarjeta 3: Dos cargas
                TarjetaEstilizada {
                    EncabezadoTarjeta(
                        icon = Icons.Filled.Layers,
                        titulo = "Dos cargas"
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Switch(
                                checked = simulandoError,
                                onCheckedChange = { viewModel.cambiarSimularError(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = SwitchTrackChecked,
                                    uncheckedThumbColor = TextSecondary,
                                    uncheckedTrackColor = ProgressTrack
                                )
                            )
                            Text(
                                text = "Simular error",
                                color = TextPrimary
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BotonOutlinedCustom(
                                onClick = { viewModel.cargarUnaPorUna() },
                                enabled = carga !is CargaUi.Cargando,
                                text = "Una por una"
                            )
                            BotonGradient(
                                onClick = { viewModel.cargarALaVez() },
                                enabled = carga !is CargaUi.Cargando,
                                text = "A la vez"
                            )
                        }

                        if (carga is CargaUi.Cargando) {
                            CircularProgressIndicator(color = ProgressFill)
                            Text(
                                text = "Cargando...",
                                color = TextPrimary
                            )
                        }

                        val estadoCarga = carga
                        if (estadoCarga is CargaUi.Exito) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                color = SuccessGreenBg
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp)
                                ) {
                                    val filas = listOf(
                                        "Modo:" to estadoCarga.modo,
                                        "Perfil:" to estadoCarga.perfil,
                                        "Pedidos:" to estadoCarga.pedidos.toString(),
                                        "Tiempo:" to "${estadoCarga.milisegundos} ms"
                                    )

                                    filas.forEachIndexed { indice, (etiqueta, valor) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = etiqueta,
                                                modifier = Modifier.weight(1f),
                                                color = CardTitleColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = valor,
                                                textAlign = TextAlign.End,
                                                color = if (indice == 0) SuccessGreenText else TextPrimary
                                            )
                                        }

                                        if (indice < filas.lastIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 6.dp),
                                                color = SuccessGreenText.copy(alpha = 0.18f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (estadoCarga is CargaUi.Error) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.errorContainer
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = estadoCarga.mensaje,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                            BotonOutlinedCustom(
                                onClick = { viewModel.cargarALaVez() },
                                text = "Reintentar",
                                icon = Icons.Filled.Refresh
                            )
                        }

                        if (historialTiempos.isNotEmpty()) {
                            TablaComparacionTiempos(historialTiempos)
                        }
                    }
                }

                // Tarjeta 4: Historial
                TarjetaEstilizada {
                    EncabezadoTarjeta(
                        icon = Icons.Filled.History,
                        titulo = "Historial"
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        if (historial.isEmpty()) {
                            Text(
                                text = "Aún no hay resultados",
                                color = TextSecondary
                            )
                        } else {
                            historial.forEachIndexed { indice, item ->
                                Text(
                                    text = item,
                                    color = TextPrimary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )

                                if (indice < historial.lastIndex) {
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = IconBoxBg
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

