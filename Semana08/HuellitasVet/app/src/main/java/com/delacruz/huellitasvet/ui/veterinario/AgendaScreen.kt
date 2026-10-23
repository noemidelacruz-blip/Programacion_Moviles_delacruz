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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.CitaCard
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.fechaCorta
import com.delacruz.huellitasvet.ui.components.fechaDia
import com.delacruz.huellitasvet.ui.components.nombreCorto
import com.delacruz.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val ES_PE = Locale.forLanguageTag("es-PE")

// Agenda del veterinario: selector de fecha (chips de la semana o calendario del mes) y
// linea de tiempo con las citas del dia elegido. La View solo observa el ViewModel
@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onVolver: () -> Unit,
    onAtender: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val vistaMes by vm.vistaMes.collectAsState()
    val mesVisible by vm.mesVisible.collectAsState()
    val diasConCitas by vm.diasConCitas.collectAsState()
    val hoy = LocalDate.now()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            titulo = "Agenda",
            onAtras = onVolver,
            acciones = {
                // Alterna entre los chips de la semana y el calendario del mes
                IconButton(onClick = { vm.alternarVista() }) {
                    Icon(
                        imageVector = if (vistaMes) Icons.AutoMirrored.Filled.ViewList
                        else Icons.Filled.CalendarMonth,
                        contentDescription = if (vistaMes) "Ver vista de semana"
                        else "Ver vista de mes"
                    )
                }
            }
        )

        // Una sola lista para que el selector y las citas se deslicen juntos
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                if (vistaMes) {
                    CalendarioMes(
                        mes = mesVisible,
                        seleccionada = fecha,
                        diasConCitas = diasConCitas,
                        onAnterior = { vm.mesAnterior() },
                        onSiguiente = { vm.mesSiguiente() },
                        onElegir = { vm.elegirFecha(it) }
                    )
                } else {
                    ChipsSemana(
                        dias = vm.dias,
                        seleccionada = fecha,
                        hoy = hoy,
                        onElegir = { vm.elegirFecha(it) }
                    )
                }
            }

            item {
                Text(
                    text = "Citas del ${fechaCorta(fecha.toString())}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 8.dp)
                )
            }

            if (citas.isEmpty()) {
                item {
                    Text(
                        text = "No hay citas para este día",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
                    )
                }
            } else {
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

// Chips de los proximos dias: hoy y los 6 siguientes
@Composable
private fun ChipsSemana(
    dias: List<LocalDate>,
    seleccionada: LocalDate,
    hoy: LocalDate,
    onElegir: (LocalDate) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dias) { dia ->
            ChipDia(
                dia = dia,
                seleccionado = dia == seleccionada,
                esHoy = dia == hoy,
                onClick = { onElegir(dia) }
            )
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

// Calendario del mes: encabezado con flechas, iniciales de los dias y cuadricula de 7 columnas
@Composable
private fun CalendarioMes(
    mes: YearMonth,
    seleccionada: LocalDate,
    diasConCitas: Map<String, Int>,
    onAnterior: () -> Unit,
    onSiguiente: () -> Unit,
    onElegir: (LocalDate) -> Unit
) {
    val hoy = LocalDate.now()

    // Celdas vacias antes del dia 1: la semana empieza en lunes (DayOfWeek.MONDAY = 1)
    val vacias = mes.atDay(1).dayOfWeek.value - 1
    val totalDias = mes.lengthOfMonth()
    val filas = (vacias + totalDias + 6) / 7

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        // Encabezado: mes anterior, nombre del mes y el anio, mes siguiente
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onAnterior) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Mes anterior"
                )
            }
            Text(
                text = tituloMes(mes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onSiguiente) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Mes siguiente"
                )
            }
        }

        // Iniciales de los dias de la semana, de lunes a domingo
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { inicial ->
                Text(
                    text = inicial,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )
            }
        }

        // Cuadricula del mes: celdas vacias antes del dia 1 y luego los dias
        repeat(filas) { fila ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { columna ->
                    val indice = fila * 7 + columna
                    if (indice in vacias until (vacias + totalDias)) {
                        val dia = mes.atDay(indice - vacias + 1)
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CeldaDia(
                                dia = dia,
                                seleccionado = dia == seleccionada,
                                esHoy = dia == hoy,
                                tieneCitas = (diasConCitas[dia.toString()] ?: 0) > 0,
                                onClick = { onElegir(dia) }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// Celda de un dia: numero centrado y punto debajo si el dia tiene citas
@Composable
private fun CeldaDia(
    dia: LocalDate,
    seleccionado: Boolean,
    esHoy: Boolean,
    tieneCitas: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .then(
                    if (seleccionado) Modifier.background(MaterialTheme.colorScheme.primary)
                    else Modifier
                )
                .then(
                    if (esHoy) Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dia.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (seleccionado || esHoy) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    seleccionado -> MaterialTheme.colorScheme.onPrimary
                    esHoy -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
        // Punto de las citas: ocupa el espacio aunque el dia no tenga citas
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(6.dp)
                .clip(CircleShape)
                .then(
                    if (tieneCitas) Modifier.background(
                        if (seleccionado) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.primary
                    )
                    else Modifier
                )
        )
    }
}

// Nombre del mes y el anio con la primera letra en mayuscula: "Octubre 2026"
private fun tituloMes(mes: YearMonth): String {
    val nombre = mes.month.getDisplayName(TextStyle.FULL, ES_PE)
        .replaceFirstChar { it.uppercase() }
    return "$nombre ${mes.year}"
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

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(IntrinsicSize.Min)
    ) {
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
// Boton pequeno en forma de pildora (Confirmar o Atender)
@Composable
private fun BotonAccion(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp) // <-- Reducido a 12.dp para dar espacio
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium, // <-- Ajustado a labelMedium
            fontWeight = FontWeight.Bold,
            maxLines = 1,     // <-- Prohíbe saltos de línea
            softWrap = false  // <-- Asegura que la palabra vaya en una sola línea
        )
    }
}
