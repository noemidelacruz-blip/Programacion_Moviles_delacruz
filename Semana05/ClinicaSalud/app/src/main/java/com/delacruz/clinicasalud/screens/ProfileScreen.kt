package com.delacruz.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onMenuClick: () -> Unit
) {
    // -------------------------------------------------------------
    // ESTADOS DE INFORMACIÓN DEL PACIENTE
    // -------------------------------------------------------------
    var nombre by remember { mutableStateOf("Noemí de la Cruz") }
    var correo by remember { mutableStateOf("noemi.delacruz@email.com") }
    var dni by remember { mutableStateOf("76543210") }
    var telefono by remember { mutableStateOf("+51 987 654 321") }
    var direccion by remember { mutableStateOf("Av. Las Flores 123, Lima") }
    var tipoSangre by remember { mutableStateOf("O Rh+") }

    // Estado para controlar la visibilidad del diálogo de edición
    var mostrarDialogoEdicion by remember { mutableStateOf(false) }

    // Variables temporales para cuando el usuario esté editando
    var tempNombre by remember { mutableStateOf(nombre) }
    var tempTelefono by remember { mutableStateOf(telefono) }
    var tempCorreo by remember { mutableStateOf(correo) }
    var tempDireccion by remember { mutableStateOf(direccion) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menú lateral"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar con iniciales
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "NC",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre y correo
            Text(
                text = nombre,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = correo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta: Información Personal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Información Personal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FilaInfoPerfil(
                        icono = Icons.Default.Badge,
                        titulo = "DNI / Identificación",
                        valor = dni
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.4f))

                    FilaInfoPerfil(
                        icono = Icons.Default.Phone,
                        titulo = "Teléfono móvil",
                        valor = telefono
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.4f))

                    FilaInfoPerfil(
                        icono = Icons.Default.Home,
                        titulo = "Dirección de domicilio",
                        valor = direccion
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.4f))

                    FilaInfoPerfil(
                        icono = Icons.Default.Favorite,
                        titulo = "Tipo de sangre",
                        valor = tipoSangre
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de acción (Editar Perfil)
            OutlinedButton(
                onClick = {
                    // Preparamos los valores temporales y abrimos el diálogo
                    tempNombre = nombre
                    tempCorreo = correo
                    tempTelefono = telefono
                    tempDireccion = direccion
                    mostrarDialogoEdicion = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar información")
            }
        }
    }

    // -------------------------------------------------------------
    // DIÁLOGO (POP-UP) PARA EDITAR INFORMACIÓN
    // -------------------------------------------------------------
    if (mostrarDialogoEdicion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEdicion = false },
            title = {
                Text(
                    text = "Editar Perfil",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = tempNombre,
                        onValueChange = { tempNombre = it },
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = tempCorreo,
                        onValueChange = { tempCorreo = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = tempTelefono,
                        onValueChange = { tempTelefono = it },
                        label = { Text("Teléfono móvil") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = tempDireccion,
                        onValueChange = { tempDireccion = it },
                        label = { Text("Dirección de domicilio") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Guardamos los datos modificados
                        nombre = tempNombre
                        correo = tempCorreo
                        telefono = tempTelefono
                        direccion = tempDireccion
                        mostrarDialogoEdicion = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoEdicion = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Componente reutilizable para cada elemento de información en el perfil.
 */
@Composable
fun FilaInfoPerfil(
    icono: ImageVector,
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}