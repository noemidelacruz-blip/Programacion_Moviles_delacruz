package com.delacruz.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.model.Sexo
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.VetBotonTipo
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.components.VetTextField
import com.delacruz.huellitasvet.ui.components.fechaDia
import com.delacruz.huellitasvet.ui.components.iconoDeServicio
import com.delacruz.huellitasvet.ui.components.nombreCorto
import com.delacruz.huellitasvet.ui.components.textoEdad
import com.delacruz.huellitasvet.viewmodel.AtenderCitaViewModel
import java.util.Locale

// Pantalla Atender cita del veterinario. La View solo muestra los campos y avisa de los clics:
// las validaciones y la transaccion viven en el ViewModel y el Repository
@Composable
fun AtenderCitaScreen(
    vm: AtenderCitaViewModel,
    onVolver: () -> Unit
) {
    val cita by vm.cita.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val listo by vm.listo.collectAsState()

    var peso by rememberSaveable { mutableStateOf("") }
    var diagnostico by rememberSaveable { mutableStateOf("") }
    var tratamiento by rememberSaveable { mutableStateOf("") }
    val snackbar = remember { SnackbarHostState() }

    // Como en el mockup, el peso viene con el peso actual de la mascota y el veterinario lo corrige
    var pesoCargado by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(cita) {
        val m = cita?.mascota?.mascota
        if (m != null && !pesoCargado) {
            peso = m.peso.toString()
            pesoCargado = true
        }
    }

    // Cuando la atencion se registra (o la cita se cancela), se vuelve a la Agenda
    LaunchedEffect(listo) {
        if (listo) {
            vm.consumirListo()
            onVolver()
        }
    }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar(titulo = "Atender cita", onAtras = onVolver) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val c = cita
            if (c != null) {
                val mascota = c.mascota.mascota
                val estado = EstadoCita.desdeTexto(c.cita.estado)
                // Si la cita ya no es atendible se avisa y se ocultan los campos
                // (mientras se vuelve a la Agenda tras registrar, no se muestra el aviso)
                val bloqueada = !vm.puedeAtender(c) && !listo
                val pesoNuevo = peso.trim().replace(',', '.').toDoubleOrNull()

                // Cabecera: avatar, mascota, edad, sexo, dueno, servicio, fecha y hora
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        MascotaAvatar(
                            especie = Especie.desdeTexto(mascota.especie),
                            nombre = mascota.nombre,
                            tamano = 56.dp
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "${mascota.nombre} · ${descripcionAnimal(mascota)}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${textoEdad(mascota.anioNacimiento)} · " +
                                        "${Sexo.desdeTexto(mascota.sexo).etiqueta} · " +
                                        "Dueño: ${nombreCorto(c.mascota.dueno.nombre)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = iconoDeServicio(c.servicio.icono),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = " ${c.servicio.nombre} · ${fechaDia(c.cita.fecha)} · ${c.cita.hora}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                if (bloqueada) {
                    Text(
                        text = "Esta cita ya no se puede atender (${estado.etiqueta})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    VetButton(texto = "Volver", onClick = onVolver)
                } else {
                    Text(
                        text = "Registro clínico",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Peso nuevo y, al lado, el peso anterior guardado de la mascota
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        VetTextField(
                            valor = peso,
                            onCambio = { peso = it },
                            etiqueta = "Peso (kg)",
                            tipoTeclado = KeyboardType.Decimal,
                            error = errores.peso,
                            modifier = Modifier.weight(1f)
                        )
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 14.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Peso anterior",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = " " + String.format(Locale.US, "%.1f", mascota.peso) + " kg",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    VetTextField(
                        valor = diagnostico,
                        onCambio = { diagnostico = it },
                        etiqueta = "Diagnóstico",
                        lineasMinimas = 3,
                        error = errores.diagnostico
                    )
                    VetTextField(
                        valor = tratamiento,
                        onCambio = { tratamiento = it },
                        etiqueta = "Tratamiento",
                        lineasMinimas = 3
                    )

                    // Aviso de que se actualizara el peso (aparece cuando el peso es valido)
                    if (pesoNuevo != null && pesoNuevo > 0.0) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Se actualizará el peso de ${mascota.nombre} a " +
                                            String.format(Locale.US, "%.1f", pesoNuevo) + " kg",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    VetButton(
                        texto = "Registrar atención",
                        onClick = { vm.registrar(diagnostico, tratamiento, peso) }
                    )
                    VetButton(
                        texto = "Cancelar cita",
                        onClick = { vm.cancelar() },
                        tipo = VetBotonTipo.CONTORNO,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Tipo de animal segun el sexo: Perro / Perra, Gato / Gata, seguido de la raza ("Gata siamés")
private fun descripcionAnimal(m: Mascota): String {
    val hembra = Sexo.desdeTexto(m.sexo) == Sexo.HEMBRA
    val tipo = when (Especie.desdeTexto(m.especie)) {
        Especie.PERRO -> if (hembra) "Perra" else "Perro"
        Especie.GATO -> if (hembra) "Gata" else "Gato"
        Especie.OTRO -> "Otro"
    }
    return "$tipo ${m.raza.lowercase()}"
}