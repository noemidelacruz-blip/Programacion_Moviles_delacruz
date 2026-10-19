package com.delacruz.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.fechaDiaMes
import com.delacruz.huellitasvet.ui.components.nombreCorto
import com.delacruz.huellitasvet.ui.components.textoEdad
import com.delacruz.huellitasvet.viewmodel.FiltroPaciente
import com.delacruz.huellitasvet.viewmodel.PacienteItem
import com.delacruz.huellitasvet.viewmodel.PacientesViewModel

// Pantalla Pacientes del veterinario: buscador, chips por especie y tarjetas de las mascotas.
// La View solo observa el ViewModel y avisa de los clics
@Composable
fun PacientesScreen(
    vm: PacientesViewModel,
    onVolver: () -> Unit,
    onPaciente: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pacientes by vm.pacientes.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val filtro by vm.filtro.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(titulo = "Pacientes", onAtras = onVolver)

        // Buscador por nombre de mascota o de dueno
        OutlinedTextField(
            value = busqueda,
            onValueChange = { vm.buscar(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            placeholder = { Text("Buscar mascota o dueño…") },
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // Chips Todos / Perros / Gatos / Otros
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FiltroPaciente.entries.forEach { f ->
                ChipFiltro(
                    texto = f.etiqueta,
                    seleccionado = f == filtro,
                    onClick = { vm.elegirFiltro(f) }
                )
            }
        }

        if (pacientes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No se encontraron pacientes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, top = 4.dp, end = 20.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(pacientes, key = { it.mascota.mascota.id }) { p ->
                    TarjetaPaciente(p = p, onClick = { onPaciente(p.mascota.mascota.id) })
                }
            }
        }
    }
}

// Chip de filtro en forma de pildora: el elegido se rellena con el color primario
@Composable
private fun ChipFiltro(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
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
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Tarjeta de un paciente: avatar, nombre, raza y edad, dueno, ultima visita y flecha
@Composable
private fun TarjetaPaciente(p: PacienteItem, onClick: () -> Unit) {
    val m = p.mascota.mascota
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MascotaAvatar(
                especie = Especie.desdeTexto(m.especie),
                nombre = m.nombre,
                tamano = 56.dp
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = m.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${m.raza} · ${textoEdad(m.anioNacimiento)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Dueño: ${nombreCorto(p.mascota.dueno.nombre)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (p.ultimaVisita != null) "Última visita: ${fechaDiaMes(p.ultimaVisita)}"
                    else "Sin visitas",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}