package com.delacruz.authfirebaseapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Destinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
}

@Composable
fun AuthApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN
    ) {
        // 1. Ruta de Inicio de Sesión
        composable(Destinations.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Destinations.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 2. Ruta de Registro
        composable(Destinations.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // 3. Ruta de Pantalla Principal (Home)
        composable(Destinations.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}