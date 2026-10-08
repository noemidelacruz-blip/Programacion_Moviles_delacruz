package com.delacruz.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

// Vista sin diseño de referencia: sigue el estilo del Registro.
@Composable
fun LoginScreen(
    navController: NavController
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var intentado by remember { mutableStateOf(false) }
    var errorLogin by remember { mutableStateOf<String?>(null) }

    val errorUsuario = if (usuario.isBlank()) "Ingresa tu teléfono o correo" else null
    val errorPassword = if (password.isBlank()) "Ingresa tu contraseña" else null

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
            text = "Iniciar sesión",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Ingresa para gestionar tus citas",
            fontSize = 15.sp,
            color = TextoSecundario
        )

        Spacer(modifier = Modifier.height(28.dp))

        CampoTexto(
            etiqueta = "Teléfono o correo",
            valor = usuario,
            onValorChange = { usuario = it },
            icono = Icons.Default.Person,
            tipoTeclado = KeyboardType.Email,
            error = if (intentado) errorUsuario else null
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

        errorLogin?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        BotonPrimario(
            texto = "Ingresar",
            onClick = {
                intentado = true
                errorLogin = null
                if (errorUsuario == null && errorPassword == null) {
                    if (Repositorio.iniciarSesion(usuario, password)) {
                        // Al entrar al Inicio, Atrás ya no vuelve al login.
                        navController.navigate(Rutas.Home.ruta) {
                            popUpTo(Rutas.Splash.ruta) { inclusive = true }
                        }
                    } else {
                        errorLogin = "Teléfono, correo o contraseña incorrectos"
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextoConEnlace(
            texto = "¿No tienes cuenta? ",
            enlace = "Regístrate",
            onClick = {
                navController.navigate(Rutas.Registro.ruta) {
                    popUpTo(Rutas.Login.ruta) { inclusive = true }
                }
            }
        )
    }
}