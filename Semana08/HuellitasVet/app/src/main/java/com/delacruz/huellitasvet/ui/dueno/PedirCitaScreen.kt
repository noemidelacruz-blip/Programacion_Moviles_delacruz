package com.delacruz.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.Horarios
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.FechaChip
import com.delacruz.huellitasvet.ui.components.HoraChip
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.components.fechaCorta
import com.delacruz.huellitasvet.ui.components.iconoDeServicio
import com.delacruz.huellitasvet.ui.components.textoPrecio
import com.delacruz.huellitasvet.viewmodel.PedirCitaViewModel

// Pantalla Pedir cita. La View solo muestra lo elegido y avisa de los clics:
// las reglas (fechas, horarios, servicios activos) viven en el ViewModel
@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    onVolver: () -> Unit,
    onCitaCreada: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val mascotaId by vm.mascotaId.collectAsState()
    val servicioId by vm.servicioId.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val creada by vm.creada.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    // Cuando la cita se crea, se vuelve a Mis citas
    LaunchedEffect(creada) {
        if (creada) {
            vm.consumirEventos()
            onCitaCreada()
        }
    }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    val mascota = mascotas.firstOrNull { it.id == mascotaId }
    val servicio = servicios.firstOrNull { it.id == servicioId }
    val horaElegida = hora
    // El boton se habilita solo cuando todo esta elegido
    val completo = mascota != null && servicio != null && horaElegida != null

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar(titulo = "Pedir cita", onAtras = onVolver) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---------- Quien viene ----------
            Titulo("¿Quién viene?")
            if (mascotas.isEmpty()) {
                Text(
                    text = "Primero registra una mascota para pedir una cita",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(mascotas, key = { it.id }) { m ->
                        ChipMascota(
                            m = m,
                            seleccionada = m.id == mascotaId,
                            onClick = { vm.elegirMascota(m.id) }
                        )
                    }
                }
            }

            // ---------- Servicio ----------
            Titulo("Servicio")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                servicios.forEach { s ->
                    TarjetaServicio(
                        s = s,
                        seleccionado = s.id == servicioId,
                        onClick = { vm.elegirServicio(s.id) }
                    )
                }
            }

            // ---------- Fecha: de hoy a 7 dias ----------
            Titulo("Fecha")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { dia ->
                    FechaChip(
                        fecha = dia,
                        seleccionada = dia == fecha,
                        onClick = { vm.elegirFecha(dia) }
                    )
                }
            }

            // ---------- Hora: grilla de 4 columnas ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Titulo("Hora")
                Text(
                    text = "Ocupado = gris",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Horarios.todos.chunked(4).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { h ->
                            HoraChip(
                                hora = h,
                                seleccionada = h == horaElegida,
                                ocupada = h in ocupadas,
                                onClick = { vm.elegirHora(h) },
                                // Si la fecha es hoy, las horas que ya pasaron quedan apagadas
                                habilitada = !vm.esHoraPasada(fecha, h),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Completa la ultima fila para que todos los chips midan lo mismo
                        repeat(4 - fila.size) { Spacer(modifier = Modifier.weight(1f)) }
                    }
                }
            }

            // ---------- Resumen ----------
            if (mascota != null && servicio != null && horaElegida != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "${mascota.nombre} · ${servicio.nombre}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${fechaCorta(fecha.toString())} · $horaElegida · " +
                                    textoPrecio(servicio.precio, decimales = true),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            VetButton(
                texto = "Confirmar cita",
                onClick = { vm.confirmar() },
                habilitado = completo
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Titulo de cada seccion
@Composable
private fun Titulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

// Chip de mascota con su avatar: la elegida lleva fondo claro y borde del color primario
@Composable
private fun ChipMascota(m: Mascota, seleccionada: Boolean, onClick: () -> Unit) {
    val forma = RoundedCornerShape(50)
    Row(
        modifier = Modifier
            .clip(forma)
            .background(
                if (seleccionada) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .border(
                width = if (seleccionada) 1.5.dp else 1.dp,
                color = if (seleccionada) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = forma
            )
            .clickable(onClick = onClick)
            .padding(start = 8.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MascotaAvatar(
            especie = Especie.desdeTexto(m.especie),
            nombre = m.nombre,
            tamano = 34.dp,
            fondo = if (seleccionada) Color.White else MaterialTheme.colorScheme.primaryContainer
        )
        Text(
            text = m.nombre,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (seleccionada) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Tarjeta de servicio: icono, nombre, duracion y precio a la derecha
@Composable
private fun TarjetaServicio(s: Servicio, seleccionado: Boolean, onClick: () -> Unit) {
    val forma = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(
                if (seleccionado) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .border(
                width = if (seleccionado) 1.5.dp else 1.dp,
                color = if (seleccionado) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = forma
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = iconoDeServicio(s.icono),
            contentDescription = null,
            tint = if (seleccionado) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = s.nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${s.duracionMin} min",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = textoPrecio(s.precio),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}