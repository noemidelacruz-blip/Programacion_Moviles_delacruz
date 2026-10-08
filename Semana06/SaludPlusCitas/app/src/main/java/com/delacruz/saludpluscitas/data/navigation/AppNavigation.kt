package com.delacruz.saludpluscitas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.delacruz.saludpluscitas.ui.screens.agendamiento.CitaExitosaScreen
import com.delacruz.saludpluscitas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.delacruz.saludpluscitas.ui.screens.agendamiento.EspecialidadesScreen
import com.delacruz.saludpluscitas.ui.screens.agendamiento.FechaHoraScreen
import com.delacruz.saludpluscitas.ui.screens.agendamiento.MedicosScreen
import com.delacruz.saludpluscitas.ui.screens.auth.LoginScreen
import com.delacruz.saludpluscitas.ui.screens.auth.RegistroScreen
import com.delacruz.saludpluscitas.ui.screens.auth.SplashScreen
import com.delacruz.saludpluscitas.ui.screens.auth.TerminosScreen
import com.delacruz.saludpluscitas.ui.screens.citas.DetalleCitaScreen
import com.delacruz.saludpluscitas.ui.screens.citas.MisCitasScreen
import com.delacruz.saludpluscitas.ui.screens.home.HomeScreen
import com.delacruz.saludpluscitas.ui.screens.notificaciones.NotificacionesScreen
import com.delacruz.saludpluscitas.ui.screens.perfil.PerfilScreen
import com.delacruz.saludpluscitas.ui.screens.resultados.ResultadosScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {

        // auth
        composable(Rutas.Splash.ruta) {
            SplashScreen(navController)
        }

        composable(Rutas.Registro.ruta) {
            RegistroScreen(navController)
        }

        composable(Rutas.Login.ruta) {
            LoginScreen(navController)
        }

        composable(Rutas.Terminos.ruta) {
            TerminosScreen(navController)
        }

        // home
        composable(Rutas.Home.ruta) {
            HomeScreen(navController)
        }

        // agendamiento
        composable(Rutas.Especialidades.ruta) {
            EspecialidadesScreen(navController)
        }

        composable(
            route = Rutas.Medicos.ruta,
            arguments = listOf(
                navArgument("especialidadId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getInt("especialidadId") ?: 0
            MedicosScreen(
                navController = navController,
                especialidadId = especialidadId
            )
        }

        composable(
            route = Rutas.FechaHora.ruta,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            FechaHoraScreen(
                navController = navController,
                medicoId = medicoId
            )
        }

        composable(
            route = Rutas.ConfirmarCita.ruta,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            ConfirmarCitaScreen(
                navController = navController,
                medicoId = medicoId,
                fecha = fecha,
                hora = hora
            )
        }

        composable(
            route = Rutas.CitaExitosa.ruta,
            arguments = listOf(
                navArgument("citaId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            CitaExitosaScreen(
                navController = navController,
                citaId = citaId
            )
        }

        // citas
        composable(Rutas.MisCitas.ruta) {
            MisCitasScreen(navController)
        }

        composable(
            route = Rutas.DetalleCita.ruta,
            arguments = listOf(
                navArgument("citaId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            DetalleCitaScreen(
                navController = navController,
                citaId = citaId
            )
        }

        // perfil, resultados y notificaciones
        composable(Rutas.Perfil.ruta) {
            PerfilScreen(navController)
        }

        composable(Rutas.Resultados.ruta) {
            ResultadosScreen(navController)
        }

        composable(Rutas.Notificaciones.ruta) {
            NotificacionesScreen(navController)
        }
    }
}