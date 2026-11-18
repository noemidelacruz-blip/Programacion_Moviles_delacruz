package com.delacruz.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.centrotareas.model.CargaUi
import com.delacruz.centrotareas.model.PrimosUi
import com.delacruz.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    var toques by rememberSaveable { mutableIntStateOf(0) }
    var textoHasta by rememberSaveable { mutableStateOf("") }
    var hastaContado by rememberSaveable { mutableIntStateOf(0) }
    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simulandoError by viewModel.simularError.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CentroTareas",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Tarea con progreso",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(onClick = { toques++ }) {
                            Text("Tócame: $toques")
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.tareaLarga() },
                                enabled = !corriendo
                            ) {
                                Text("Iniciar")
                            }
                            OutlinedButton(
                                onClick = { viewModel.cancelar() },
                                enabled = corriendo
                            ) {
                                Text("Cancelar")
                            }
                        }
                        LinearProgressIndicator(
                            progress = { progreso },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(text = "${(progreso * 100).toInt()} %")
                        Text(text = mensaje)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Contar números primos",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = textoHasta,
                            onValueChange = { entrada ->
                                textoHasta = entrada.filter { it.isDigit() }
                            },
                            label = { Text("Hasta el número") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val hasta = textoHasta.toIntOrNull()
                                    if (hasta != null && hasta >= 2) {
                                        hastaContado = hasta
                                        viewModel.contar(hasta)
                                    }
                                },
                                enabled = primos !is PrimosUi.Calculando
                            ) {
                                Text("Contar")
                            }
                            OutlinedButton(
                                onClick = { viewModel.cancelarPrimos() },
                                enabled = primos is PrimosUi.Calculando
                            ) {
                                Text("Cancelar")
                            }
                        }
                        val estadoPrimos = primos
                        if (estadoPrimos is PrimosUi.Listo) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFDDF0DA)
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
                                        color = Color(0xFF1B5E20),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Tiempo: ${estadoPrimos.milisegundos} ms",
                                        color = Color(0xFF1B5E20)
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = when (estadoPrimos) {
                                    PrimosUi.Inicial -> "Ingresa un número mayor o igual a 2"
                                    PrimosUi.Calculando -> "Calculando..."
                                    PrimosUi.Cancelado -> "Cancelado"
                                    is PrimosUi.Listo -> ""
                                }
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Dos cargas",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Switch(
                                checked = simulandoError,
                                onCheckedChange = { viewModel.cambiarSimularError(it) }
                            )
                            Text(text = "Simular error")
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.cargarUnaPorUna() },
                                enabled = carga !is CargaUi.Cargando
                            ) {
                                Text("Una por una")
                            }
                            Button(
                                onClick = { viewModel.cargarALaVez() },
                                enabled = carga !is CargaUi.Cargando
                            ) {
                                Text("A la vez")
                            }
                        }

                        if (carga is CargaUi.Cargando) {
                            CircularProgressIndicator()
                            Text(text = "Cargando...")
                        }

                        val estadoCarga = carga
                        if (estadoCarga is CargaUi.Exito) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFDDF0DA)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = estadoCarga.modo,
                                        color = Color(0xFF1B5E20),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Perfil: ${estadoCarga.perfil}",
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = "Pedidos: ${estadoCarga.pedidos}",
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = "Tiempo: ${estadoCarga.milisegundos} ms",
                                        color = Color(0xFF1B5E20)
                                    )
                                }
                            }
                        }

                        if (estadoCarga is CargaUi.Error) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.errorContainer
                            ) {
                                Text(
                                    text = estadoCarga.mensaje,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            OutlinedButton(onClick = { viewModel.cargarALaVez() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}
