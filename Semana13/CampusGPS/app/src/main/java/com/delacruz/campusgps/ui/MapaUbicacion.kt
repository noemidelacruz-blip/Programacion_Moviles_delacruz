package com.delacruz.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.delacruz.campusgps.model.LugarConDistancia
import com.delacruz.campusgps.model.UbicacionUi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // TODO Paso 3: LaunchedEffect(estado): si es Lista, animar la cámara (zoom 16f)

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        // TODO Paso 3: si el estado es Lista, un Marker "Estás aquí"
        // TODO Paso 4: un Marker por cada lugar
    }
}
