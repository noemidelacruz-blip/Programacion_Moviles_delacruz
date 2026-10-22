package com.delacruz.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Vacuna
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.EstadoVacuna
import com.delacruz.huellitasvet.model.Sexo
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.HistorialItem
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.VetBotonTipo
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.components.VetTextField
import com.delacruz.huellitasvet.ui.components.fechaDiaMes
import com.delacruz.huellitasvet.ui.components.textoEdad
import com.delacruz.huellitasvet.ui.theme.EstadoCancelada
import com.delacruz.huellitasvet.ui.theme.EstadoConfirmada
import com.delacruz.huellitasvet.ui.theme.EstadoPendiente
import com.delacruz.huellitasvet.viewmodel.MascotaViewModel
import java.util.Locale

// Ficha de la mascota: cabecera, botones Pedir cita y Editar, y pestanas Datos / Historial / Vacunas.
// La View solo observa el ViewModel y avisa de los clics
@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int,
    onVolver: () -> Unit,
    onPedirCita: (() -> Unit)? = null,
    onEditar: (() -> Unit)? = null,
    puedeRegistrarVacunas: Boolean = false,
    modifier: Modifier = Modifier
) {
    val mascota by vm.mascota(mascotaId).collectAsState()
    val historial by vm.historial(mascotaId).collectAsState()
    val vacunasAlDia by vm.vacunasAlDia(mascotaId).collectAsState()
    // 0 = Datos, 1 = Historial, 2 = Vacunas
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
                } else if (pestana == 1) {
                    if (historial.isEmpty()) {
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
                } else if (pestana == 2) {
                    PestanaVacunas(
                        vm = vm,
                        mascotaId = m.id,
                        puedeRegistrarVacunas = puedeRegistrarVacunas
                    )
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

// Pestanas Datos / Historial / Vacunas: la activa va en una pildora blanca con texto del color primario
@Composable
private fun SelectorPestanas(seleccion: Int, onElegir: (Int) -> Unit) {
    val nombres = listOf("Datos", "Historial", "Vacunas")
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

// Pestana Datos: especie, raza, edad, sexo, peso y ano de nacimiento
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

// Pestana Vacunas: muestra las vacunas registradas y permite registrar nuevas si puedeRegistrarVacunas es true
@Composable
private fun PestanaVacunas(
    vm: MascotaViewModel,
    mascotaId: Int,
    puedeRegistrarVacunas: Boolean
) {
    val vacunas by vm.vacunas(mascotaId).collectAsState()
    var mostrarDialogo by rememberSaveable { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (puedeRegistrarVacunas) {
            VetButton(
                texto = "Registrar vacuna",
                onClick = {
                    vm.limpiarErrorVacuna()
                    mostrarDialogo = true
                },
                icono = Icons.Filled.Add
            )
        }

        if (vacunas.isEmpty()) {
            Text(
                text = "Aún no tiene vacunas registradas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        } else {
            vacunas.forEach { v ->
                TarjetaVacuna(v = v, estado = vm.estadoVacuna(v))
            }
        }
    }

    if (mostrarDialogo) {
        DialogoRegistrarVacuna(
            vm = vm,
            mascotaId = mascotaId,
            onDismiss = { mostrarDialogo = false }
        )
    }
}

// Tarjeta de una vacuna con su nombre, fecha de aplicacion, proxima dosis y chip de estado
@Composable
private fun TarjetaVacuna(
    v: Vacuna,
    estado: EstadoVacuna
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = v.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Aplicada: ${fechaDiaMes(v.fecha)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (v.proximaDosis != null) {
                    Text(
                        text = "Próxima dosis: ${fechaDiaMes(v.proximaDosis)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            EstadoVacunaChip(estado = estado)
        }
    }
}

// Chip de estado para la vacuna: Vigente (verde), Proxima (ambar) o Vencida (rojo)
@Composable
private fun EstadoVacunaChip(
    estado: EstadoVacuna,
    modifier: Modifier = Modifier
) {
    val color = when (estado) {
        EstadoVacuna.VIGENTE -> EstadoConfirmada
        EstadoVacuna.PROXIMA -> EstadoPendiente
        EstadoVacuna.VENCIDA -> EstadoCancelada
    }
    Box(
        modifier = modifier
            .background(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = estado.etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold,
            softWrap = false,
            maxLines = 1
        )
    }
}

// Dialogo para registrar una nueva vacuna
@Composable
private fun DialogoRegistrarVacuna(
    vm: MascotaViewModel,
    mascotaId: Int,
    onDismiss: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var opcionMeses by rememberSaveable { mutableStateOf<Int?>(null) }
    val errorVacuna by vm.errorVacuna.collectAsState()
    val vacunaGuardada by vm.vacunaGuardada.collectAsState()

    LaunchedEffect(vacunaGuardada) {
        if (vacunaGuardada) {
            vm.consumirVacunaGuardada()
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = {
            vm.limpiarErrorVacuna()
            onDismiss()
        },
        title = {
            Text(
                text = "Registrar vacuna",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                VetTextField(
                    valor = nombre,
                    onCambio = {
                        nombre = it
                        if (errorVacuna != null) vm.limpiarErrorVacuna()
                    },
                    etiqueta = "Nombre de la vacuna",
                    error = errorVacuna
                )

                Text(
                    text = "Próxima dosis",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val opciones = listOf(
                    null to "Sin próxima dosis",
                    1 to "En 1 mes",
                    3 to "En 3 meses",
                    6 to "En 6 meses",
                    12 to "En 12 meses"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ChipOpcionProxima(
                            texto = opciones[0].second,
                            seleccionado = opcionMeses == opciones[0].first,
                            onClick = { opcionMeses = opciones[0].first }
                        )
                        ChipOpcionProxima(
                            texto = opciones[1].second,
                            seleccionado = opcionMeses == opciones[1].first,
                            onClick = { opcionMeses = opciones[1].first }
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ChipOpcionProxima(
                            texto = opciones[2].second,
                            seleccionado = opcionMeses == opciones[2].first,
                            onClick = { opcionMeses = opciones[2].first }
                        )
                        ChipOpcionProxima(
                            texto = opciones[3].second,
                            seleccionado = opcionMeses == opciones[3].first,
                            onClick = { opcionMeses = opciones[3].first }
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ChipOpcionProxima(
                            texto = opciones[4].second,
                            seleccionado = opcionMeses == opciones[4].first,
                            onClick = { opcionMeses = opciones[4].first }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    vm.registrarVacuna(mascotaId, nombre, opcionMeses)
                }
            ) {
                Text(
                    text = "Guardar",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    vm.limpiarErrorVacuna()
                    onDismiss()
                }
            ) {
                Text(
                    text = "Cancelar",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

// Chip para elegir el tiempo de la proxima dosis
@Composable
private fun ChipOpcionProxima(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (seleccionado) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .border(
                width = 1.dp,
                color = if (seleccionado) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            color = if (seleccionado) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
