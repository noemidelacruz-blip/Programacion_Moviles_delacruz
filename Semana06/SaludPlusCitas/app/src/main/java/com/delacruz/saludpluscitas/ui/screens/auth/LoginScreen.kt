package com.delacruz.saludpluscitas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.PantallaEnConstruccion

// TODO: campos de teléfono o correo y contraseña.
//       Validaciones y Repositorio.iniciarSesion.
@Composable
fun LoginScreen(
    navController: NavController
) {
    PantallaEnConstruccion(
        titulo = "8. Iniciar sesión",
        detalle = "",
        "Ingresar" to {
            navController.navigate(Rutas.Home.ruta) {
                popUpTo(Rutas.Splash.ruta) { inclusive = true }
            }
        },
        "Regístrate" to { navController.navigate(Rutas.Registro.ruta) }
    )
}