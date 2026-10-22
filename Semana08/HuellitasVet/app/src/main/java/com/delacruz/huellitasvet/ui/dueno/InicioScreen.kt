package com.delacruz.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.ui.components.IconoCuadrado
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.components.fechaCorta
import com.delacruz.huellitasvet.ui.components.iconoDeServicio
import com.delacruz.huellitasvet.ui.theme.AmbarSuave
import com.delacruz.huellitasvet.ui.theme.AmbarTexto
import com.delacruz.huellitasvet.ui.theme.EstadoPendiente
import com.delacruz.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

// Inicio del dueno. La View solo observa el ViewModel y avisa de los clics
@Composable
fun InicioScreen(
    vm: InicioViewModel,
    nombre: String,
    onVerMascotas: () -> Unit,
    onMascota: (Int) -> Unit,
    onAgregar: () -> Unit,
    onPedirCita: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mascotas by vm.mascotas.collectAsState()
    val proxima by vm.proximaCita.collectAsState()
    val control by vm.proximoControl.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Saludo con la campana (sin pantalla de notificaciones en el laboratorio)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Hola, $nombre",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "¿Cómo están tus mascotas hoy?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        TarjetaProximaCita(cita = proxima)

        // Mis mascotas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Mis mascotas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onVerMascotas) {
                Text(text = "Ver todas", style = MaterialTheme.typography.labelMedium)
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(mascotas, key = { it.id }) { m ->
                MascotaTile(m = m, onClick = { onMascota(m.id) })
            }
            item { AgregarTile(onClick = onAgregar) }
        }

        // Accesos rapidos a los servicios: todos llevan a Pedir cita
        Text(
            text = "Servicios",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AccesoServicio("Consulta", "heart", onPedirCita, Modifier.weight(1f))
            AccesoServicio("Vacunas", "syringe", onPedirCita, Modifier.weight(1f))
            AccesoServicio("Baño", "scissors", onPedirCita, Modifier.weight(1f))
        }

        // Recordatorio calculado a partir de la proxima cita
        if (proxima != null) {
            TarjetaRecordatorio(cita = proxima!!)
        }

        // Tarjeta de proximo control medico
        if (control != null) {
            TarjetaProximoControl(cita = control!!)
        }

        VetButton(texto = "Pedir una cita", onClick = onPedirCita)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// Tarjeta verde con la proxima cita, o un mensaje si no hay ninguna
@Composable
private fun TarjetaProximaCita(cita: CitaConDetalle?) {
    if (cita == null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Aún no tienes citas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Pide una cita para tu mascota",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        return
    }

    val mascota = cita.mascota.mascota
    val estado = EstadoCita.desdeTexto(cita.cita.estado)
    val textoClaro = MaterialTheme.colorScheme.onPrimary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Próxima cita",
                    style = MaterialTheme.typography.labelMedium,
                    color = textoClaro.copy(alpha = 0.8f)
                )
                Text(
                    text = "${mascota.nombre} · ${cita.servicio.nombre}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textoClaro
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = textoClaro,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${fechaCorta(cita.cita.fecha)} · ${cita.cita.hora}",
                        style = MaterialTheme.typography.bodySmall,
                        color = textoClaro
                    )
                }
                // Chip de estado claro sobre el fondo verde
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.22f), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = estado.etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = textoClaro
                    )
                }
            }
            MascotaAvatar(
                especie = Especie.desdeTexto(mascota.especie),
                nombre = mascota.nombre,
                tamano = 60.dp,
                fondo = Color.White
            )
        }
    }
}

// Una mascota del carrusel: avatar, nombre y "Ver ficha"
@Composable
private fun MascotaTile(m: Mascota, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(84.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotaAvatar(
            especie = Especie.desdeTexto(m.especie),
            nombre = m.nombre,
            tamano = 64.dp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = m.nombre,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Ver ficha",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Ultimo elemento del carrusel: "+ Agregar" una mascota
@Composable
private fun AgregarTile(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(84.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(18.dp))
                .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Agregar mascota",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Agregar",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Acceso rapido a un servicio (icono y nombre)
@Composable
private fun AccesoServicio(
    nombre: String,
    icono: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconoCuadrado(icono = iconoDeServicio(icono))
            Text(
                text = nombre,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Aviso ambar con un recordatorio de la proxima cita
@Composable
private fun TarjetaRecordatorio(cita: CitaConDetalle) {
    // Cuando es la cita: hoy, manana o dentro de N dias
    val dias = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(cita.cita.fecha))
    val cuando = when (dias) {
        0L -> "hoy a las ${cita.cita.hora}"
        1L -> "mañana a las ${cita.cita.hora}"
        else -> "en $dias días"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AmbarSuave)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = null,
                tint = EstadoPendiente
            )
            Column {
                Text(
                    text = "Recordatorio",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = AmbarTexto
                )
                Text(
                    text = "${cita.mascota.mascota.nombre}: ${cita.servicio.nombre} $cuando",
                    style = MaterialTheme.typography.bodySmall,
                    color = AmbarTexto
                )
            }
        }
    }
}

// Tarjeta proximo control si hay un control programado
@Composable
private fun TarjetaProximoControl(cita: CitaConDetalle) {
    val fc = cita.cita.fechaControl ?: return
    val fechaControlLocal = LocalDate.parse(fc)
    val dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaControlLocal)
    val cuando = when (dias) {
        0L -> "hoy"
        1L -> "mañana"
        else -> "en $dias días"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = "Próximo control",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${cita.mascota.mascota.nombre} · ${fechaCorta(fc)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = cuando,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}