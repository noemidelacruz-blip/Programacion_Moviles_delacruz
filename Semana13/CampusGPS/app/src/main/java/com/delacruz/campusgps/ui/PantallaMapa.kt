package com.delacruz.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.campusgps.model.LugarConDistancia
import com.delacruz.campusgps.model.UbicacionUi
import com.delacruz.campusgps.util.formatearDistancia
import com.delacruz.campusgps.viewmodel.CampusViewModelFactory
import com.delacruz.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa() {
    val contexto = LocalContext.current
    val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(contexto))

    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val categoria by vm.categoria.collectAsState()

    // El más cercano con su distancia, para el texto debajo del botón
    val cercano = lugares.firstOrNull { it.lugar.id == masCercano?.id }

    val hayError = estado is UbicacionUi.Error

    // Las olas decorativas cambian de tono con el tema oscuro
    val oscuro = isSystemInDarkTheme()

    // Android dibuja el cuadro de permiso; aquí solo se pide y se lee el resultado
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun tienePermiso(): Boolean =
        ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                contexto,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

    fun alPulsar() {
        if (tienePermiso()) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { _ ->
        Column(modifier = Modifier.fillMaxSize()) {
            // Cabecera con degradado vino y olas dibujadas con Canvas
            OlasSuperiores(altura = 110.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, top = 14.dp)
                ) {
                    Text(
                        text = "CampusGPS",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Los 5 lugares del campus",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            if (hayError) {
                // Sin mapa no hay panel: los controles y el mensaje van arriba,
                // justo debajo de la cabecera, y las olas se quedan al fondo
                BloqueControles(
                    estado = estado,
                    cercano = cercano,
                    hayError = true,
                    onPulsar = { alPulsar() },
                    onIrAlMasCercano = { masCercano?.let { vm.seleccionar(it.id) } },
                    onReintentar = { vm.buscar() }
                )
                Spacer(Modifier.weight(1f))
                OlasInferiores(altura = 200.dp, oscuro = oscuro)
            } else {
                MapaUbicacion(
                    estado = estado,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercano = masCercano,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                // Panel inferior con esquinas superiores redondeadas y olas detrás del contenido
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 12.dp
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OlasInferiores(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            altura = 200.dp,
                            oscuro = oscuro
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                        ) {
                            BloqueControles(
                                estado = estado,
                                cercano = cercano,
                                hayError = false,
                                onPulsar = { alPulsar() },
                                onIrAlMasCercano = { masCercano?.let { vm.seleccionar(it.id) } },
                                onReintentar = { vm.buscar() }
                            )

                            // Chips que filtran el mapa y la lista
                            ChipsCategoria(
                                categorias = vm.categorias,
                                seleccionada = categoria,
                                onSeleccionar = { vm.filtrarPorCategoria(it) },
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )

                            Spacer(Modifier.height(6.dp))

                            ListaLugares(
                                lugares = lugares,
                                seleccionado = seleccionado,
                                onClick = { vm.seleccionar(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                idMasCercano = masCercano?.id
                            )
                        }
                    }
                }
            }
        }
    }
}

// Botón principal, botón del más cercano y texto de estado.
// Se usa en el panel inferior y, cuando hay error, directamente bajo la cabecera.
@Composable
private fun BloqueControles(
    estado: UbicacionUi,
    cercano: LugarConDistancia?,
    hayError: Boolean,
    onPulsar: () -> Unit,
    onIrAlMasCercano: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = if (hayError) 20.dp else 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BotonUbicacion(
            habilitado = estado !is UbicacionUi.Buscando,
            onClick = onPulsar
        )

        if (!hayError) {
            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = onIrAlMasCercano,
                enabled = cercano != null,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.5.dp,
                    color = if (cercano != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Ir al más cercano")
            }

            if (cercano != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Más cercano: ${cercano.lugar.nombre} " +
                        "(${cercano.metros?.let { formatearDistancia(it) } ?: "Sin calcular"})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(if (hayError) 14.dp else 6.dp))

        when (estado) {
            is UbicacionUi.Inicial -> Text(
                text = "Pulsa el botón para empezar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            is UbicacionUi.Buscando -> Text(
                text = "Buscando tu ubicación...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            is UbicacionUi.Lista -> {
                Text(
                    text = "Latitud: ${estado.posicion.latitud}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Longitud: ${estado.posicion.longitud}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is UbicacionUi.Error -> MensajeError(
                error = estado,
                onReintentar = onReintentar
            )
        }
    }
}
