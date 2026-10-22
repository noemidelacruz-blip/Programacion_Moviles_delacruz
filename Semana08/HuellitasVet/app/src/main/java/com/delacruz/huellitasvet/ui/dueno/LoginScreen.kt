package com.delacruz.huellitasvet.ui.dueno

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.model.Rol
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.components.VetTextField
import com.delacruz.huellitasvet.ui.theme.VetTheme
import com.delacruz.huellitasvet.viewmodel.SesionViewModel
import kotlinx.coroutines.launch

// Login usado por los dos perfiles. El color de toda la pantalla (selector, iconos, links y
// boton) sigue al perfil elegido: esmeralda para Dueno y azul para Veterinario.
// La View solo observa el ViewModel y le avisa de los clics
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginExitoso: (Usuario) -> Unit
) {
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()
    val modoOscuro by vm.modoOscuro.collectAsState()

    // Estado local de la pantalla: perfil elegido y texto de los campos
    var rolElegido by rememberSaveable { mutableStateOf(Rol.CLIENTE.name) }
    val rol = Rol.desdeTexto(rolElegido)
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Al abrir la pantalla no debe quedar un error viejo
    LaunchedEffect(Unit) { vm.limpiarError() }

    // Cuando el login es correcto, el usuario actual deja de ser null y se avisa a la navegacion
    LaunchedEffect(usuario) {
        usuario?.let(onLoginExitoso)
    }

    VetTheme(rol = rol, modoOscuro = modoOscuro) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            snackbarHost = { SnackbarHost(snackbar) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ingresa para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))
                SelectorRol(
                    seleccionado = rol,
                    onElegir = {
                        rolElegido = it.name
                        vm.limpiarError()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
                VetTextField(
                    valor = correo,
                    onCambio = {
                        correo = it
                        vm.limpiarError()
                    },
                    etiqueta = "Correo",
                    icono = Icons.Outlined.Email,
                    tipoTeclado = KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(12.dp))
                VetTextField(
                    valor = clave,
                    onCambio = {
                        clave = it
                        vm.limpiarError()
                    },
                    etiqueta = "Contraseña",
                    icono = Icons.Outlined.Lock,
                    esClave = true
                )

                // "Olvidaste tu contraseña" no tiene pantalla en el laboratorio: solo avisa
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {
                        scope.launch { snackbar.showSnackbar("No disponible en modo práctica") }
                    }) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Mensaje de error del ViewModel
                if (error != null) {
                    Text(
                        text = error ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                VetButton(
                    texto = "Ingresar",
                    onClick = { vm.login(correo, clave, rol) }
                )

                Spacer(modifier = Modifier.height(16.dp))
                if (rol == Rol.CLIENTE) {
                    // Solo el dueno ve el registro (tampoco tiene pantalla en el laboratorio)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes cuenta?",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = {
                            scope.launch { snackbar.showSnackbar("No disponible en modo práctica") }
                        }) {
                            Text(
                                text = "Regístrate",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // Solo el veterinario ve la tarjeta "Modo practica"
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Modo práctica",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// Selector de dos mitades: la elegida se rellena con el color del perfil
@Composable
private fun SelectorRol(
    seleccionado: Rol,
    onElegir: (Rol) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf(Rol.CLIENTE, Rol.ADMIN).forEach { rol ->
            val activo = rol == seleccionado
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (activo) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onElegir(rol) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rol.etiqueta,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (activo) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}