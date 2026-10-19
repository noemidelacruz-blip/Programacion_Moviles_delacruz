package com.delacruz.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.Sexo
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.HistorialItem
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.VetBotonTipo
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.components.fechaDiaMes
import com.delacruz.huellitasvet.ui.components.textoEdad
import com.delacruz.huellitasvet.viewmodel.MascotaViewModel
import java.util.Locale

// Ficha de la mascota: cabecera, botones Pedir cita y Editar, y pestanas Datos / Historial clinico.
// La View solo observa el ViewModel y avisa de los clics
@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int,
    onVolver: () -> Unit,
    onPedirCita: (() -> Unit)? = null,
    onEditar: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val mascota by vm.mascota(mascotaId).collectAsState()
    val historial by vm.historial(mascotaId).collectAsState()
    val vacunasAlDia by vm.vacunasAlDia(mascotaId).collectAsState()
    // 0 = Datos, 1 = Historial clinico (el mockup abre en Historial)
    var pestana by rememberSaveable { mutableIntStateOf(1) }

    val m = mascota

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(titulo = m?.nombre ?: "", onAtras = onVolver)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (m != null) {
                CabeceraMascota(m = m, vacunasAlDia = vacunasAlDia)

                // Los botones son del dueno: el veterinario abre la ficha sin ellos
                if (onPedirCita != null && onEditar != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        VetButton(
                            texto = "Pedir cita",
                            onClick = onPedirCita,
                            icono = Icons.Filled.CalendarMonth,
                            modifier = Modifier.weight(1f)
                        )
                        VetButton(
                            texto = "Editar",
                            onClick = onEditar,
                            tipo = VetBotonTipo.CONTORNO,
                            icono = Icons.Filled.Edit,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                SelectorPestanas(seleccion = pestana, onElegir = { pestana = it })

                if (pestana == 0) {
                    DatosMascota(m = m)
                } else if (historial.isEmpty()) {
                    // Sin atenciones registradas
                    Text(
                        text = "Aún no tiene atenciones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                } else {
                    // Linea de tiempo: la mas reciente arriba (el DAO ya las ordena asi)
                    historial.forEachIndexed { indice, c ->
                        HistorialItem(
                            fecha = fechaDiaMes(c.cita.fecha),
                            servicio = c.servicio.nombre,
                            diagnostico = c.cita.diagnostico,
                            peso = c.cita.pesoRegistrado,
                            esUltimo = indice == historial.lastIndex
                        )
                    }
                }
            }
        }
    }
}

// Texto de la especie segun el sexo: Perro / Perra, Gato / Gata
private fun tipoAnimal(m: Mascota): String {
    val hembra = Sexo.desdeTexto(m.sexo) == Sexo.HEMBRA
    return when (Especie.desdeTexto(m.especie)) {
        Especie.PERRO -> if (hembra) "Perra" else "Perro"
        Especie.GATO -> if (hembra) "Gata" else "Gato"
        Especie.OTRO -> "Otro"
    }
}

private fun textoPeso(peso: Double): String = String.format(Locale.US, "%.1f", peso) + " kg"

// Tarjeta superior: avatar, nombre, tipo y raza, edad, sexo, peso actual y chip de vacunas
@Composable
private fun CabeceraMascota(m: Mascota, vacunasAlDia: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MascotaAvatar(
                especie = Especie.desdeTexto(m.especie),
                nombre = m.nombre,
                tamano = 80.dp
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = m.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${tipoAnimal(m)} ${m.raza.lowercase()} · ${textoEdad(m.anioNacimiento)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${Sexo.desdeTexto(m.sexo).etiqueta} · ${textoPeso(m.peso)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (vacunasAlDia) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Vacunas al día",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// Pestanas Datos / Historial clinico: la activa va en una pildora blanca con texto del color primario
@Composable
private fun SelectorPestanas(seleccion: Int, onElegir: (Int) -> Unit) {
    val nombres = listOf("Datos", "Historial clínico")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
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

// Pestana Datos: especie, raza, edad, sexo, peso y año de nacimiento
@Composable
private fun DatosMascota(m: Mascota) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilaDato("Especie", Especie.desdeTexto(m.especie).etiqueta)
            FilaDato("Raza", m.raza)
            FilaDato("Edad", textoEdad(m.anioNacimiento))
            FilaDato("Sexo", Sexo.desdeTexto(m.sexo).etiqueta)
            FilaDato("Peso actual", textoPeso(m.peso))
            FilaDato("Año de nacimiento", m.anioNacimiento.toString())
        }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}