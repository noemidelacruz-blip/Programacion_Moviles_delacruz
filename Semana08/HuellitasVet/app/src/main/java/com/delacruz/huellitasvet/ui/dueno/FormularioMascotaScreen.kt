package com.delacruz.huellitasvet.ui.dueno

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.Sexo
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.VetBotonTipo
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.components.VetTextField
import com.delacruz.huellitasvet.viewmodel.MascotaViewModel

// Formulario para registrar una mascota nueva (mascotaId = 0) o editar una existente.
// La View solo muestra los campos y avisa al ViewModel; las validaciones viven en el ViewModel
@Composable
fun FormularioMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int,
    onVolver: () -> Unit,
    onEliminada: () -> Unit
) {
    val esEdicion = mascotaId != 0
    val existente by vm.mascota(mascotaId).collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val guardado by vm.guardado.collectAsState()
    val eliminada by vm.eliminada.collectAsState()

    // Estado de los campos. Especie y sexo se guardan como texto del enum (o null si no se eligio)
    var nombre by rememberSaveable { mutableStateOf("") }
    var especie by rememberSaveable { mutableStateOf<String?>(null) }
    var raza by rememberSaveable { mutableStateOf("") }
    var anio by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf<String?>(null) }
    var cargado by rememberSaveable { mutableStateOf(false) }
    var confirmarEliminar by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }

    // Al editar, se rellenan los campos una sola vez con los datos guardados
    LaunchedEffect(existente) {
        val m = existente
        if (esEdicion && m != null && !cargado) {
            nombre = m.nombre
            especie = m.especie
            raza = m.raza
            anio = m.anioNacimiento.toString()
            peso = m.peso.toString()
            sexo = m.sexo
            cargado = true
        }
    }
    // Reacciones a los eventos del ViewModel
    LaunchedEffect(guardado) {
        if (guardado) {
            vm.consumirEventos()
            onVolver()
        }
    }
    LaunchedEffect(eliminada) {
        if (eliminada) {
            vm.consumirEventos()
            onEliminada()
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
                titulo = if (esEdicion) "Editar mascota" else "Nueva mascota",
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
            // Avatar segun la especie elegida, con la insignia "+" del mockup
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                MascotaAvatar(
                    especie = especie?.let { Especie.desdeTexto(it) } ?: Especie.OTRO,
                    nombre = nombre,
                    tamano = 96.dp
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(30.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            VetTextField(
                valor = nombre,
                onCambio = { nombre = it },
                etiqueta = "Nombre",
                error = errores.nombre
            )

            // Chips de especie (el elegido va relleno)
            Text(
                text = "Especie",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Especie.entries.forEach { e ->
                    ChipOpcion(
                        texto = e.etiqueta,
                        seleccionado = especie == e.name,
                        relleno = true,
                        onClick = { especie = e.name },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            errores.especie?.let { ErrorTexto(it) }

            VetTextField(
                valor = raza,
                onCambio = { raza = it },
                etiqueta = "Raza"
            )

            // Año de nacimiento y peso lado a lado
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VetTextField(
                    valor = anio,
                    onCambio = { anio = it },
                    etiqueta = "Año de nacimiento",
                    tipoTeclado = KeyboardType.Number,
                    error = errores.anio,
                    modifier = Modifier.weight(1f)
                )
                VetTextField(
                    valor = peso,
                    onCambio = { peso = it },
                    etiqueta = "Peso (kg)",
                    tipoTeclado = KeyboardType.Decimal,
                    error = errores.peso,
                    modifier = Modifier.weight(1f)
                )
            }

            // Chips de sexo (el elegido va con fondo claro y borde)
            Text(
                text = "Sexo",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Sexo.entries.forEach { s ->
                    ChipOpcion(
                        texto = s.etiqueta,
                        seleccionado = sexo == s.name,
                        relleno = false,
                        onClick = { sexo = s.name },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            errores.sexo?.let { ErrorTexto(it) }

            Spacer(modifier = Modifier.height(4.dp))
            VetButton(
                texto = "Guardar mascota",
                onClick = {
                    vm.guardar(
                        id = mascotaId,
                        nombre = nombre,
                        especie = especie?.let { Especie.desdeTexto(it) },
                        raza = raza,
                        anio = anio,
                        peso = peso,
                        sexo = sexo?.let { Sexo.desdeTexto(it) }
                    )
                }
            )
            VetButton(texto = "Cancelar", onClick = onVolver, tipo = VetBotonTipo.CONTORNO)

            // Solo al editar: eliminar la mascota (pide confirmacion)
            if (esEdicion) {
                VetButton(
                    texto = "Eliminar mascota",
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
            title = { Text("Eliminar mascota") },
            text = { Text("¿Seguro que quieres eliminar a ${existente?.nombre ?: "esta mascota"}?") },
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

// Mensaje de error pequeno debajo de un grupo de chips
@Composable
private fun ErrorTexto(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(start = 4.dp)
    )
}

// Chip de opcion. relleno = true: el elegido se rellena del color primario (especie).
// relleno = false: el elegido lleva fondo claro y borde del color primario (sexo)
@Composable
private fun ChipOpcion(
    texto: String,
    seleccionado: Boolean,
    relleno: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(14.dp)
    val colorFondo = when {
        seleccionado && relleno -> MaterialTheme.colorScheme.primary
        seleccionado -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val colorTexto = when {
        seleccionado && relleno -> MaterialTheme.colorScheme.onPrimary
        seleccionado -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val colorBorde =
        if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(forma)
            .background(colorFondo)
            .border(
                width = if (seleccionado && !relleno) 1.5.dp else 1.dp,
                color = colorBorde,
                shape = forma
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )
    }
}