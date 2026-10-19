package com.delacruz.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.CitaCard
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.fechaDia
import com.delacruz.huellitasvet.ui.components.nombreCorto
import com.delacruz.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate

// Agenda del dia del veterinario: chips de dias y linea de tiempo por hora.
// La View solo observa el ViewModel y avisa de los clics
@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onVolver: () -> Unit,
    onAtender: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val hoy = LocalDate.now()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(titulo = "Agenda", onAtras = onVolver)

        // Chips de los proximos dias
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(vm.dias) { dia ->
                ChipDia(
                    dia = dia,
                    seleccionado = dia == fecha,
                    esHoy = dia == hoy,
                    onClick = { vm.elegirFecha(dia) }
                )
            }
        }

        if (citas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No hay citas para este día",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 16.dp)
            ) {
                items(citas, key = { it.cita.id }) { c ->
                    FilaAgenda(
                        c = c,
                        onConfirmar = { vm.confirmar(c) },
                        onAtender = { onAtender(c.cita.id) }
                    )
                }
            }
        }
    }
}

// Chip de dia en forma de pildora: el elegido se rellena con el color primario
@Composable
private fun ChipDia(
    dia: LocalDate,
    seleccionado: Boolean,
    esHoy: Boolean,
    onClick: () -> Unit
) {
    val forma = RoundedCornerShape(50)
    Box(
        modifier = Modifier
            .clip(forma)
            .background(
                if (seleccionado) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )
            .border(
                width = 1.dp,
                color = if (seleccionado) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = forma
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (esHoy) "Hoy ${dia.dayOfMonth}" else fechaDia(dia.toString()),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

// Una fila de la linea de tiempo: la hora a la izquierda, la linea vertical y la tarjeta de la cita
@Composable
private fun FilaAgenda(
    c: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit
) {
    val estado = EstadoCita.desdeTexto(c.cita.estado)
    val mascota = c.mascota.mascota

    // PENDIENTE -> Confirmar, CONFIRMADA -> Atender; ATENDIDA y CANCELADA no tienen accion
    val accion: (@Composable () -> Unit)? = when (estado) {
        EstadoCita.PENDIENTE -> {
            { BotonAccion(texto = "Confirmar", onClick = onConfirmar) }
        }
        EstadoCita.CONFIRMADA -> {
            { BotonAccion(texto = "Atender", onClick = onAtender) }
        }
        else -> null
    }

    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Text(
            text = c.cita.hora,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(52.dp)
                .padding(top = 14.dp)
        )
        // Linea vertical de la linea de tiempo
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Spacer(modifier = Modifier.width(10.dp))
        CitaCard(
            titulo = "${mascota.nombre} · ${c.servicio.nombre}",
            detalle = "Dueño: ${nombreCorto(c.mascota.dueno.nombre)}",
            estado = estado,
            izquierda = {
                MascotaAvatar(
                    especie = Especie.desdeTexto(mascota.especie),
                    nombre = mascota.nombre,
                    tamano = 44.dp
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp),
            accion = accion
        )
    }
}

// Boton pequeno en forma de pildora (Confirmar o Atender)
@Composable
private fun BotonAccion(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}