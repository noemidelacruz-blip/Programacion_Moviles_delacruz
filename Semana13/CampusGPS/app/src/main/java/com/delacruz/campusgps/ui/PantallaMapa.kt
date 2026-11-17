package com.delacruz.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.delacruz.campusgps.model.UbicacionUi
import com.delacruz.campusgps.viewmodel.CampusViewModelFactory
import com.delacruz.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa() {
    val contexto = LocalContext.current
    val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(contexto))

    val estado by vm.estado.collectAsState()

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
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { alPulsar() },
                enabled = estado !is UbicacionUi.Buscando,
                shape = RoundedCornerShape(50)
            ) {
                Text("Obtener mi ubicación")
            }

            Spacer(Modifier.height(12.dp))

            when (val actual = estado) {
                is UbicacionUi.Inicial -> Text(
                    text = "Pulsa el botón para empezar",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                is UbicacionUi.Buscando -> Text(
                    text = "Buscando tu ubicación...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                is UbicacionUi.Lista -> {
                    Text("Latitud: ${actual.posicion.latitud}")
                    Text("Longitud: ${actual.posicion.longitud}")
                }

                is UbicacionUi.Error -> Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
