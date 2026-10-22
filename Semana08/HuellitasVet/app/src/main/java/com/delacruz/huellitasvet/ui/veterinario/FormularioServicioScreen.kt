package com.delacruz.huellitasvet.ui.veterinario

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.VetBotonTipo
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.components.VetTextField
import com.delacruz.huellitasvet.ui.components.iconoDeServicio
import com.delacruz.huellitasvet.viewmodel.ServiciosViewModel
import java.util.Locale

// Formulario para crear un servicio (servicioId = 0) o editar uno existente.
// La View solo muestra los campos y avisa al ViewModel; las validaciones viven en el ViewModel
@Composable
fun FormularioServicioScreen(
    vm: ServiciosViewModel,
    servicioId: Int,
    onVolver: () -> Unit
) {
    val esEdicion = servicioId != 0
    val existente by vm.servicioEdicion.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val listo by vm.listo.collectAsState()

    // Estado de los campos
    var nombre by rememberSaveable { mutableStateOf("") }
    var duracion by rememberSaveable { mutableIntStateOf(30) }
    var precio by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var icono by rememberSaveable { mutableStateOf("heart") }
    var activo by rememberSaveable { mutableStateOf(true) }
    var cargado by rememberSaveable { mutableStateOf(false) }
    var confirmarEliminar by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }

    // Al editar, se pide el servicio al ViewModel y se rellenan los campos una sola vez
    LaunchedEffect(servicioId) { vm.cargar(servicioId) }
    LaunchedEffect(existente) {
        val s = existente
        if (esEdicion && s != null && !cargado) {
            nombre = s.nombre
            duracion = s.duracionMin
            precio = String.format(Locale.US, "%.2f", s.precio)
            descripcion = s.descripcion
            icono = s.icono
            activo = s.activo
            cargado = true
        }
    }
    // Cuando se guarda o se elimina, se vuelve a la lista
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
        topBar = {
            AppTopBar(
                titulo = if (esEdicion) "Editar servicio" else "Nuevo servicio",
                onAtras = onVolver
            )
        },
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
            VetTextField(
                valor = nombre,
                onCambio = { nombre = it },
                etiqueta = "Nombre del servicio",
                error = errores.nombre
            )

            // Duracion: 15, 30, 45 o 60 minutos
            Text(
                text = "Duración",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ServiciosViewModel.DURACIONES.forEach { minutos ->
                    ChipDuracion(
                        texto = "$minutos min",
                        seleccionado = duracion == minutos,
                        onClick = { duracion = minutos },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            errores.duracion?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            VetTextField(
                valor = precio,
                onCambio = { precio = it },
                etiqueta = "Precio (S/)",
                tipoTeclado = KeyboardType.Decimal,
                error = errores.precio
            )
            VetTextField(
                valor = descripcion,
                onCambio = { descripcion = it },
                etiqueta = "Descripción",
                lineasMinimas = 3
            )

            // Icono del servicio: corazon, jeringa o tijeras
            Text(
                text = "Ícono",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("heart", "syringe", "scissors").forEach { clave ->
                    OpcionIcono(
                        clave = clave,
                        seleccionado = icono == clave,
                        onClick = { icono = clave },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Servicio activo: los duenos solo pueden pedir los activos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Servicio activo",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Los dueños podrán pedirlo",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = activo, onCheckedChange = { activo = it })
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            VetButton(
                texto = "Guardar servicio",
                onClick = {
                    // El precio se lee como numero; si no es valido queda en 0 y el ViewModel lo rechaza
                    val precioNumero = precio.trim().replace(',', '.').toDoubleOrNull() ?: 0.0
                    vm.guardar(
                        Servicio(
                            id = servicioId,
                            nombre = nombre,
                            descripcion = descripcion,
                            duracionMin = duracion,
                            precio = precioNumero,
                            activo = activo,
                            icono = icono
                        )
                    )
                }
            )
            VetButton(texto = "Cancelar", onClick = onVolver, tipo = VetBotonTipo.CONTORNO)

            // Solo al editar: eliminar el servicio (pide confirmacion)
            if (esEdicion) {
                VetButton(
                    texto = "Eliminar servicio",
                    onClick = { confirmarEliminar = true },
                    tipo = VetBotonTipo.CONTORNO,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (confirmarEliminar) {
        AlertDialog(
            onDismissRequest = { confirmarEliminar = false },
            title = { Text("Eliminar servicio") },
            text = { Text("¿Seguro que quieres eliminar ${existente?.nombre ?: "este servicio"}?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminar = false
                    existente?.let { vm.eliminar(it) }
                }) {
                    Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminar = false }) { Text("Cancelar") }
            }
        )
    }
}

// Chip de duracion en forma de pildora: el elegido se rellena con el color primario
@Composable
private fun ChipDuracion(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .height(40.dp)
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
            .clickable(onClick = onClick),
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

// Opcion de icono: la elegida lleva fondo claro y borde del color primario
@Composable
private fun OpcionIcono(
    clave: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .height(56.dp)
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
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconoDeServicio(clave),
            contentDescription = null,
            tint = if (seleccionado) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}