package com.delacruz.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.campusgps.model.Lugar
import com.delacruz.campusgps.model.LugarConDistancia
import com.delacruz.campusgps.model.UbicacionUi
import com.delacruz.campusgps.util.formatearDistancia
import com.delacruz.campusgps.viewmodel.CampusViewModelFactory
import com.delacruz.campusgps.viewmodel.MapaViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalConfiguration

// Las dos pestañas de la app. Las dos comparten el mismo MapaViewModel.
private enum class Pestana(val etiqueta: String, val icono: ImageVector) {
    MAPA("Mapa", Icons.Filled.Map),
    CATEGORIAS("Categorías", Icons.Filled.Category)
}

@Composable
fun PantallaMapa() {
    val contexto = LocalContext.current
    // Un solo ViewModel para las dos pestañas: no hay dos estados de la pantalla
    val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(contexto))

    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val siguiendo by vm.siguiendo.collectAsState()

    // El más cercano con su distancia, para la tarjeta del mapa
    val cercano = lugares.firstOrNull { it.lugar.id == masCercano?.id }

    val hayError = estado is UbicacionUi.Error

    // Las olas decorativas cambian de tono con el tema oscuro
    val oscuro = isSystemInDarkTheme()

    // La pestaña se recuerda al girar el celular
    var pestana by rememberSaveable { mutableStateOf(Pestana.MAPA) }

    // La cámara vive en la pantalla padre: al cambiar de pestaña no se reinicia
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Cuando llega la ubicación (o se mueve en el seguimiento), la cámara la sigue.
    // Si la pestaña del mapa no está visible, animate() espera a que el mapa exista.
    LaunchedEffect(estado) {
        val actual = estado
        if (actual is UbicacionUi.Lista) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(actual.posicion.latitud, actual.posicion.longitud),
                    16f
                )
            )
        }
    }

    // Al elegir un lugar, la cámara viaja hasta él aunque la elección se haga en Categorías
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugar != null) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lugar.latitud, lugar.longitud),
                    17f
                )
            )
        }
    }

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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // Barra de pestañas compacta: 56 dp de contenido y, debajo, el espacio que
            // Android reserva para sus botones de navegación (window insets)
            val insetNav = WindowInsets.navigationBars.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            )
            val insetInferior = insetNav.asPaddingValues().calculateBottomPadding()

            NavigationBar(
                modifier = Modifier.height(48.dp + insetInferior),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
                windowInsets = insetNav
            ) {
                Pestana.entries.forEach { opcion ->
                    NavigationBarItem(
                        modifier = Modifier.height(48.dp),
                        selected = pestana == opcion,
                        onClick = { pestana = opcion },
                        icon = {
                            Icon(
                                imageVector = opcion.icono,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = opcion.etiqueta,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Cabecera con degradado vino y olas dibujadas con Canvas
            OlasSuperiores(altura = 68.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, top = 4.dp)
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
                // Los errores salen arriba, en la pestaña que sea, con las olas al fondo
                Box(modifier = Modifier.fillMaxSize()) {
                    OlasInferiores(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        altura = 120.dp,
                        oscuro = oscuro
                    )
                    Column(modifier = Modifier.fillMaxSize()) {
                        ContenidoError(
                            estado = estado,
                            onPulsar = { alPulsar() },
                            onReintentar = { vm.buscar() }
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Las mismas olas en Mapa y en Categorías: van al fondo y pegadas
                    // justo encima de la barra de pestañas
                    OlasInferiores(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        altura = 120.dp,
                        oscuro = oscuro
                    )

                    when (pestana) {
                        Pestana.MAPA -> ContenidoMapa(
                            estado = estado,
                            lugares = lugares,
                            seleccionado = seleccionado,
                            masCercano = masCercano,
                            cercano = cercano,
                            siguiendo = siguiendo,
                            camara = camara,
                            onPulsar = { alPulsar() },
                            onIrAlMasCercano = { masCercano?.let { vm.seleccionar(it.id) } },
                            onAlternarSeguimiento = { vm.alternarSeguimiento() }
                        )

                        Pestana.CATEGORIAS -> ContenidoCategorias(
                            categorias = vm.categorias,
                            categoria = categoria,
                            lugares = lugares,
                            seleccionado = seleccionado,
                            masCercano = masCercano,
                            onFiltrar = { vm.filtrarPorCategoria(it) },
                            onSeleccionarLugar = { id ->
                                vm.seleccionar(id)
                                // Al tocar un lugar se vuelve al mapa para verlo
                                pestana = Pestana.MAPA
                            }
                        )
                    }
                }
            }
        }
    }
}

// Pestaña del mapa: el mapa ocupa el resto y debajo van los botones y la tarjeta,
// dibujados encima de las olas (no hay panel blanco).
@Composable
private fun ContenidoMapa(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    cercano: LugarConDistancia?,
    siguiendo: Boolean,
    camara: CameraPositionState,
    onPulsar: () -> Unit,
    onIrAlMasCercano: () -> Unit,
    onAlternarSeguimiento: () -> Unit
) {
    val esHorizontal =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (esHorizontal) {
        Row(modifier = Modifier.fillMaxSize()) {
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                camara = camara,
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BotonUbicacion(
                    habilitado = estado !is UbicacionUi.Buscando,
                    onClick = onPulsar
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonSecundario(
                        texto = "Ir al más cercano",
                        habilitado = cercano != null,
                        resaltado = false,
                        onClick = onIrAlMasCercano
                    )

                    BotonSecundario(
                        texto = if (siguiendo) "Detener" else "Seguir en vivo",
                        habilitado = true,
                        resaltado = siguiendo,
                        onClick = onAlternarSeguimiento
                    )
                }

                val posicion = (estado as? UbicacionUi.Lista)?.posicion
                if (posicion != null) {
                    Spacer(Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Latitud: ${posicion.latitud}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Longitud: ${posicion.longitud}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (cercano != null && seleccionado == masCercano?.id) {
                    Spacer(Modifier.height(12.dp))
                    TarjetaMasCercano(cercano = cercano)
                }
            }
        }
    } else {

    Column(modifier = Modifier.fillMaxSize()) {
        MapaUbicacion(
            estado = estado,
            lugares = lugares,
            seleccionado = seleccionado,
            masCercano = masCercano,
            camara = camara,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BotonUbicacion(
                habilitado = estado !is UbicacionUi.Buscando,
                onClick = onPulsar
            )

            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonSecundario(
                    texto = "Ir al más cercano",
                    habilitado = cercano != null,
                    resaltado = false,
                    onClick = onIrAlMasCercano
                )

                // Iniciar/Detener el seguimiento en vivo
                BotonSecundario(
                    texto = if (siguiendo) "Detener" else "Seguir en vivo",
                    habilitado = true,
                    resaltado = siguiendo,
                    onClick = onAlternarSeguimiento
                )
            }

            // Latitud y longitud de la ubicación del usuario, solo cuando ya existe.
            // Van sobre una píldora opaca para que se lean bien encima de las olas.
            val posicion = (estado as? UbicacionUi.Lista)?.posicion
            if (posicion != null) {
                Spacer(Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Latitud: ${posicion.latitud}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Longitud: ${posicion.longitud}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // La tarjeta aparece solo si el lugar seleccionado es el más cercano,
            // es decir, después de pulsar "Ir al más cercano"
            if (cercano != null && seleccionado == masCercano?.id) {
                Spacer(Modifier.height(12.dp))
                TarjetaMasCercano(cercano = cercano)
            }
        }
    }
}
    }

// Pestaña de categorías: encabezado, chips de filtro y la lista con distancias.
@Composable
private fun ContenidoCategorias(
    categorias: List<String>,
    categoria: String?,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    onFiltrar: (String?) -> Unit,
    onSeleccionarLugar: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Selecciona una categoría para filtrar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(14.dp))

            // Chips: Todos, Estudio, Comida, Eventos y Servicios
            ChipsCategoria(
                categorias = categorias,
                seleccionada = categoria,
                onSeleccionar = onFiltrar
            )
        }

        Spacer(Modifier.height(6.dp))

        // La lista llega hasta la barra de pestañas, sin hueco al final
        ListaLugares(
            lugares = lugares,
            seleccionado = seleccionado,
            onClick = onSeleccionarLugar,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            idMasCercano = masCercano?.id
        )
    }
}

// Con error no hay mapa ni lista: solo el botón y el mensaje, arriba de todo
@Composable
private fun ContenidoError(
    estado: UbicacionUi,
    onPulsar: () -> Unit,
    onReintentar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BotonUbicacion(
            habilitado = estado !is UbicacionUi.Buscando,
            onClick = onPulsar
        )

        Spacer(Modifier.height(14.dp))

        if (estado is UbicacionUi.Error) {
            MensajeError(
                error = estado,
                onReintentar = onReintentar
            )
        }
    }
}

// Botón de contorno que se usa en la fila de acciones del mapa
@Composable
private fun BotonSecundario(
    texto: String,
    habilitado: Boolean,
    resaltado: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = when {
                !habilitado -> MaterialTheme.colorScheme.outlineVariant
                else -> MaterialTheme.colorScheme.primary
            }
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            // Fondo opaco: los botones quedan encima de las olas y así el texto se lee
            containerColor = if (resaltado) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            contentColor = if (resaltado) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    ) {
        Text(texto, maxLines = 1)
    }
}

// Tarjeta con el lugar más cercano y su distancia
@Composable
private fun TarjetaMasCercano(
    cercano: LugarConDistancia
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconoDeCategoria(cercano.lugar.categoria),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "MÁS CERCANO",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = cercano.lugar.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(8.dp))
            val metros = cercano.metros
            if (metros != null) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = formatearDistancia(metros),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
