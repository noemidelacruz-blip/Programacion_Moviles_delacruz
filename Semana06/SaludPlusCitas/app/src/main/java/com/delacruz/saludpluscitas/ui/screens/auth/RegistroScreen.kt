package com.delacruz.saludpluscitas.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BotonPrimario
import com.delacruz.saludpluscitas.ui.components.CampoTexto
import com.delacruz.saludpluscitas.ui.components.TextoConEnlace
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

@Composable
fun RegistroScreen(
    navController: NavController
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Los errores se muestran recién después de intentar registrarse.
    var intentado by remember { mutableStateOf(false) }
    var errorRegistro by remember { mutableStateOf<String?>(null) }

    val errorNombre = if (nombre.isBlank()) "Ingresa tu nombre completo" else null
    val errorTelefono = if (telefono.length != 9) "El teléfono debe tener 9 dígitos" else null
    val errorCorreo = if (correo.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
        "Correo no válido"
    } else {
        null
    }
    val errorPassword = if (password.length < 6) "Mínimo 6 caracteres" else null
    val formularioValido = listOf(errorNombre, errorTelefono, errorCorreo, errorPassword).all { it == null }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Crear cuenta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Regístrate para agendar tus citas",
            fontSize = 15.sp,
            color = TextoSecundario
        )

        Spacer(modifier = Modifier.height(28.dp))

        CampoTexto(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValorChange = { nombre = it },
            icono = Icons.Default.Person,
            error = if (intentado) errorNombre else null
        )
        Spacer(modifier = Modifier.height(8.dp))
        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            // Solo dígitos y máximo 9.
            onValorChange = { nuevo -> telefono = nuevo.filter { it.isDigit() }.take(9) },
            icono = Icons.Default.Phone,
            tipoTeclado = KeyboardType.Phone,
            error = if (intentado) errorTelefono else null
        )
        Spacer(modifier = Modifier.height(8.dp))
        CampoTexto(
            etiqueta = "Correo (opcional)",
            valor = correo,
            onValorChange = { correo = it },
            icono = Icons.Default.Email,
            tipoTeclado = KeyboardType.Email,
            error = if (intentado) errorCorreo else null
        )
        Spacer(modifier = Modifier.height(8.dp))
        CampoTexto(
            etiqueta = "Contraseña",
            valor = password,
            onValorChange = { password = it },
            icono = Icons.Default.Lock,
            esPassword = true,
            error = if (intentado) errorPassword else null
        )

        Spacer(modifier = Modifier.height(20.dp))

        errorRegistro?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        BotonPrimario(
            texto = "Registrarme",
            onClick = {
                intentado = true
                errorRegistro = null
                if (formularioValido) {
                    val registrado = Repositorio.registrarUsuario(nombre, telefono, correo, password)
                    if (registrado) {
                        // Tras registrarse, el usuario debe iniciar sesión; Atrás ya no vuelve al registro.
                        navController.navigate(Rutas.Login.ruta) {
                            popUpTo(Rutas.Registro.ruta) { inclusive = true }
                        }
                    } else {
                        errorRegistro = "Ese teléfono o correo ya está registrado"
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Al registrarte aceptas nuestros",
            fontSize = 13.sp,
            color = TextoSecundario
        )
        Text(
            text = "Términos y Condiciones",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = AzulPrimario,
            modifier = Modifier
                .clickable { navController.navigate(Rutas.Terminos.ruta) }
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextoConEnlace(
            texto = "¿Ya tienes cuenta? ",
            enlace = "Iniciar sesión",
            onClick = {
                navController.navigate(Rutas.Login.ruta) {
                    popUpTo(Rutas.Registro.ruta) { inclusive = true }
                }
            }
        )
    }
}