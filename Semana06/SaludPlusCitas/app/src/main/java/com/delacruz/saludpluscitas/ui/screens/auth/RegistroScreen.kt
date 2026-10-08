package com.delacruz.saludpluscitas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: formulario con nombre, teléfono, correo (opcional) y contraseña.
//       Validaciones y Repositorio.registrarUsuario.
@Composable
fun RegistroScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "2. Registro",
        detalle = "",
        "Registrarme" to {
            navController.navigate(Rutas.Home.ruta) {
                popUpTo(Rutas.Splash.ruta) { inclusive = true }
            }
        },
        "Términos y Condiciones" to { navController.navigate(Rutas.Terminos.ruta) },
        "Ya tengo una cuenta" to { navController.navigate(Rutas.Login.ruta) }
    )
}