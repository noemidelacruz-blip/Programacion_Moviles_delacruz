package com.delacruz.campusgps.ui

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.delacruz.campusgps.R
import com.delacruz.campusgps.model.Lugar
import com.delacruz.campusgps.model.LugarConDistancia
import com.delacruz.campusgps.model.UbicacionUi
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState


/*Muestra el mapa de Google Maps.
Muestra la ubicacion actual del usuario en el mapa.
Coloca los cinco marcadores de los lugares del campus.
Resalta el lugar más cercano a tu ubicación.
Permite mostrar el mapa con un estilo oscuro.*/
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    camara: CameraPositionState,
    modifier: Modifier = Modifier
) {
    // Estilo nocturno del mapa: solo se aplica cuando la app está en tema oscuro
    val contexto = LocalContext.current
    val oscuro = isSystemInDarkTheme()
    val estiloMapa = remember(oscuro) {
        if (oscuro) cargarEstiloOscuro(contexto) else null
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        // Con estilo nulo el mapa vuelve a su aspecto normal (tema claro)
        properties = MapProperties(mapStyleOptions = estiloMapa)
    ) {
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Estás aquí"
            )
        }
        // Los 5 lugares del campus van en azul; el marcador propio sigue en rojo
        lugares.forEach { item ->
            val lugar = item.lugar
            key(lugar.id) {
                val estadoMarcador = rememberMarkerState(
                    key = lugar.id.toString(),
                    position = LatLng(lugar.latitud, lugar.longitud)
                )

                // Si la tarjeta está seleccionada, el mapa muestra el nombre del lugar
                if (seleccionado == lugar.id) {
                    LaunchedEffect(seleccionado) { estadoMarcador.showInfoWindow() }
                }

                Marker(
                    state = estadoMarcador,
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (lugar.id == masCercano?.id) {
                            BitmapDescriptorFactory.HUE_GREEN
                        } else {
                            BitmapDescriptorFactory.HUE_AZURE
                        }
                    )
                )
            }
        }
    }
}

// Lee el JSON de estilo nocturno (res/raw/mapa_oscuro.json) y lo prepara para el mapa.
// Si el archivo faltara o el JSON no fuera válido, el mapa sigue con su estilo normal.
private fun cargarEstiloOscuro(contexto: Context): MapStyleOptions? =
    runCatching {
        contexto.resources.openRawResource(R.raw.mapa_oscuro).use { entrada ->
            MapStyleOptions(entrada.bufferedReader().readText())
        }
    }.getOrNull()
