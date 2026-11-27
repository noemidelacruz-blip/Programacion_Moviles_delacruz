package com.delacruz.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.delacruz.avisoscampus.model.PermisoUi
import com.delacruz.avisoscampus.notificacion.Notificador
import com.delacruz.avisoscampus.receiver.CargadorReceiver
import com.delacruz.avisoscampus.receiver.ModoAvionReceiver
import com.delacruz.avisoscampus.ui.theme.Blanco
import com.delacruz.avisoscampus.ui.theme.BordeError
import com.delacruz.avisoscampus.ui.theme.FondoDenegado
import com.delacruz.avisoscampus.ui.theme.FondoPermitido
import com.delacruz.avisoscampus.ui.theme.FondoSinPedir
import com.delacruz.avisoscampus.ui.theme.TextoDenegado
import com.delacruz.avisoscampus.ui.theme.TextoPermitido
import com.delacruz.avisoscampus.ui.theme.TextoSecundario
import com.delacruz.avisoscampus.ui.theme.TextoSinPedir
import com.delacruz.avisoscampus.ui.theme.TituloTarjeta
import com.delacruz.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val avisos by vm.avisos.collectAsState()
    val permiso by vm.permiso.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    val escuchandoCargador by vm.escuchandoCargador.collectAsState()

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            if (!mostrado) vm.noSePudoMostrar()
        }
    }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

    // El receptor se registra al activar el switch y siempre se quita en onDispose.
    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

    DisposableEffect(escuchandoCargador) {
        if (escuchandoCargador) {
            val receptor = CargadorReceiver { conectado -> vm.alCambiarCargador(conectado) }
            val filtro = IntentFilter().apply {
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
            }
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                filtro,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaFuncion("Permiso de notificaciones") {
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Estado:")
                    EtiquetaPermiso(permiso)
                }
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            vm.permisoRespondido(true)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Permitir notificaciones")
                }
            }

            mensaje?.let { texto ->
                MensajeError(texto)
                OutlinedButton(
                    onClick = {
                        // Este mensaje solo aparece en API 33+, donde esta pantalla existe.
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
                        contexto.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Abrir ajustes")
                }
            }

            TarjetaFuncion("Avisos") {
                Button(
                    onClick = { vm.avisarAhora() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Mostrar aviso")
                }
            }

            TarjetaFuncion("Recordatorio") {
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { vm.recordarEn(10) },
                        enabled = faltan == null
                    ) {
                        Text("Recordar en 10 s")
                    }
                    OutlinedButton(
                        onClick = { vm.cancelarRecordatorio() },
                        enabled = faltan != null
                    ) {
                        Text("Cancelar")
                    }
                }
                faltan?.let { segundos ->
                    Text(
                        text = "Faltan $segundos s",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            TarjetaFuncion("Receptor de anuncios") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Escuchar el modo avión")
                    Switch(
                        checked = escuchando,
                        onCheckedChange = { vm.cambiarEscucha(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Blanco,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = Color(0xFF8F7279),
                            uncheckedTrackColor = Color(0xFFF3E2E6),
                            uncheckedBorderColor = Color(0xFF8F7279)
                        )
                    )
                }
                Text(
                    text = "Activa o desactiva el modo avión del celular",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Escuchar el cargador")
                    Switch(
                        checked = escuchandoCargador,
                        onCheckedChange = { vm.cambiarEscuchaCargador(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Blanco,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = Color(0xFF8F7279),
                            uncheckedTrackColor = Color(0xFFF3E2E6),
                            uncheckedBorderColor = Color(0xFF8F7279)
                        )
                    )
                }
            }

            HistorialAvisos(avisos)
        }
    }
}

@Composable
private fun MensajeError(texto: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = FondoDenegado,
        contentColor = TextoDenegado,
        border = BorderStroke(1.dp, BordeError),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EtiquetaPermiso(permiso: PermisoUi) {
    val (texto, colorFondo, colorTexto) = when (permiso) {
        PermisoUi.SinPedir -> Triple("Sin pedir", FondoSinPedir, TextoSinPedir)
        PermisoUi.Concedido -> Triple("Permitido", FondoPermitido, TextoPermitido)
        PermisoUi.Denegado -> Triple("Denegado", FondoDenegado, TextoDenegado)
    }
    Surface(
        color = colorFondo,
        contentColor = colorTexto,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
internal fun TarjetaFuncion(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = TituloTarjeta,
                fontWeight = FontWeight.Bold
            )
            contenido()
        }
    }
}
