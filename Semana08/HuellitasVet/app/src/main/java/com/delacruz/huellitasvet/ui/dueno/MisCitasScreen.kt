package com.delacruz.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.CitaCard
import com.delacruz.huellitasvet.ui.components.IconoCuadrado
import com.delacruz.huellitasvet.ui.components.fechaCorta
import com.delacruz.huellitasvet.ui.components.fechaDia
import com.delacruz.huellitasvet.ui.components.iconoDeServicio
import com.delacruz.huellitasvet.ui.theme.AmbarSuave
import com.delacruz.huellitasvet.ui.theme.AmbarTexto
import com.delacruz.huellitasvet.ui.theme.EstadoPendiente
import com.delacruz.huellitasvet.viewmodel.MisCitasViewModel

// Pantalla Mis citas del dueno: pestanas Proximas / Historial, boton Cancelar con confirmacion
// y boton flotante para pedir una cita. La View solo observa el ViewModel y avisa de los clics
@Composable
fun MisCitasScreen(
    vm: MisCitasViewModel,
    onVolver: () -> Unit,
    onPedirCita: () -> Unit,
    modifier: Modifier = Modifier
) {
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    // 0 = Proximas, 1 = Historial
    var pestana by rememberSaveable { mutableIntStateOf(0) }
    // Cita que el dueno esta por cancelar (null = el dialogo esta cerrado)
    var citaACancelar by remember { mutableStateOf<CitaConDetalle?>(null) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(titulo = "Mis citas", onAtras = onVolver)

            PestanasCitas(
                seleccion = pestana,
                onElegir = { pestana = it },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            val lista = if (pestana == 0) proximas else historial

            if (lista.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (pestana == 0) "No tienes citas próximas"
                        else "Aún no tienes citas en el historial",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    // Espacio abajo para que el boton flotante no tape la ultima tarjeta
                    contentPadding = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(lista, key = { it.cita.id }) { c ->
                        TarjetaCita(
                            c = c,
                            proxima = pestana == 0,
                            // El boton solo aparece si la regla de las 2 horas lo permite
                            puedeCancelar = pestana == 0 && vm.puedeCancelar(c),
                            onCancelar = { citaACancelar = c }
                        )
                    }
                    if (pestana == 0) {
                        item { AvisoCancelacion() }
                    }
                }
            }
        }

        // Boton para pedir una cita
        FloatingActionButton(
            onClick = onPedirCita,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(64.dp),
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Pedir una cita",
                modifier = Modifier.size(32.dp)
            )
        }

        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Confirmacion antes de cancelar
    val aCancelar = citaACancelar
    if (aCancelar != null) {
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text(
                    "¿Seguro que quieres cancelar ${aCancelar.servicio.nombre} de " +
                            "${aCancelar.mascota.mascota.nombre} del ${fechaCorta(aCancelar.cita.fecha)} " +
                            "a las ${aCancelar.cita.hora}?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(aCancelar)
                    citaACancelar = null
                }) {
                    Text(text = "Sí, cancelar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { citaACancelar = null }) { Text("No") }
            }
        )
    }
}

// Tarjeta de una cita: icono del servicio, mascota, fecha y hora, chip de estado y, si
// corresponde, el boton Cancelar en rojo
@Composable
private fun TarjetaCita(
    c: CitaConDetalle,
    proxima: Boolean,
    puedeCancelar: Boolean,
    onCancelar: () -> Unit
) {
    // En Proximas la fecha va corta (Mie 21); en Historial lleva el mes (Mie 21 oct)
    val fecha = if (proxima) fechaDia(c.cita.fecha) else fechaCorta(c.cita.fecha)

    CitaCard(
        titulo = c.servicio.nombre,
        detalle = "${c.mascota.mascota.nombre} · $fecha · ${c.cita.hora}",
        estado = EstadoCita.desdeTexto(c.cita.estado),
        izquierda = { IconoCuadrado(icono = iconoDeServicio(c.servicio.icono)) },
        accion = {
            if (puedeCancelar) {
                TextButton(
                    onClick = onCancelar,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}

// Aviso ambar con la regla de cancelacion
@Composable
private fun AvisoCancelacion() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AmbarSuave)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                tint = EstadoPendiente
            )
            Text(
                text = "Puedes cancelar hasta 2 horas antes",
                style = MaterialTheme.typography.bodySmall,
                color = AmbarTexto
            )
        }
    }
}

// Pestanas Proximas / Historial: la activa va en una pildora blanca con texto del color primario
@Composable
private fun PestanasCitas(
    seleccion: Int,
    onElegir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val nombres = listOf("Próximas", "Historial")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        nombres.forEachIndexed { indice, nombre ->
            val activa = indice == seleccion
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (activa) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { onElegir(indice) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (activa) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}