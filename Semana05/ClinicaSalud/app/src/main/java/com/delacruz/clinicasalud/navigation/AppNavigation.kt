package com.delacruz.clinicasalud.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.delacruz.clinicasalud.screens.AppointmentScreen
import com.delacruz.clinicasalud.screens.AppointmentsScreen
import com.delacruz.clinicasalud.screens.ConfirmationScreen
import com.delacruz.clinicasalud.screens.DoctorDetailScreen
import com.delacruz.clinicasalud.screens.HomeScreen
import com.delacruz.clinicasalud.screens.MedicalHistoryScreen
import com.delacruz.clinicasalud.screens.ProfileScreen

/**
 * Composable principal encargado de la orquestacion de rutas y navegacion de la app.
 * Utiliza NavHost para gestionar los intercambios de pantalla y AppDrawer para el menu lateral.
 */
@Composable
fun AppNavigation() {
    // Controlador de navegacion principal de Jetpack Compose
    val navController = rememberNavController()

    // Envolvemos la navegacion principal dentro del AppDrawer (Menu Lateral)
    AppDrawer(
        onNavegarA = { ruta ->
            navController.navigate(ruta) {
                popUpTo(Screen.Home.route) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    ) { onClickAbrirDrawer ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route // Ruta inicial: Pantalla de Inicio
        ) {
            // -------------------------------------------------------------
            // 1. RUTA: INICIO (HOME)
            // -------------------------------------------------------------
            composable(Screen.Home.route) {
                HomeScreen(
                    onAbrirDrawer = onClickAbrirDrawer,
                    onDoctorSeleccionado = { doctor ->
                        // Navegacion hacia el detalle del doctor pasando su ID
                        navController.navigate(Screen.DoctorDetail.createRoute(doctor.id))
                    }
                )
            }

            // -------------------------------------------------------------
            // 2. RUTA: DETALLE DEL DOCTOR
            // -------------------------------------------------------------
            composable(
                route = Screen.DoctorDetail.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
                val doctor = listaMedicosPrueba.firstOrNull { it.id == doctorId } ?: listaMedicosPrueba.first()

                DoctorDetailScreen(
                    doctor = doctor,
                    onVolverInicio = { navController.popBackStack() },
                    onAgendarCitaClick = {
                        navController.navigate(Screen.Appointment.createRoute(doctor.id))
                    }
                )
            }

            // -------------------------------------------------------------
            // 3. RUTA: AGENDAR CITA
            // -------------------------------------------------------------
            composable(
                route = Screen.Appointment.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 1
                val doctor = listaMedicosPrueba.firstOrNull { it.id == doctorId } ?: listaMedicosPrueba.first()

                AppointmentScreen(
                    doctor = doctor,
                    onVolverPerfil = { navController.popBackStack() },
                    onConfirmarCita = { fecha, hora ->
                        navController.navigate(
                            Screen.Confirmation.createRoute(
                                doctorNombre = doctor.nombre,
                                fecha = fecha,
                                hora = hora
                            )
                        )
                    }
                )
            }

            // -------------------------------------------------------------
            // 4. RUTA: CONFIRMACION DE CITA
            // -------------------------------------------------------------
            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(
                    navArgument("doctorNombre") { type = NavType.StringType },
                    navArgument("fecha") { type = NavType.StringType },
                    navArgument("hora") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val doctorNombre = backStackEntry.arguments?.getString("doctorNombre") ?: ""
                val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
                val hora = backStackEntry.arguments?.getString("hora") ?: ""

                ConfirmationScreen(
                    doctorNombre = doctorNombre,
                    fecha = fecha,
                    hora = hora,
                    onVolverAtras = { navController.popBackStack() },
                    onVerMisCitasClick = {
                        navController.navigate(Screen.Appointments.route)
                    },
                    onVolverInicioClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // -------------------------------------------------------------
            // 5. RUTA: MIS CITAS (APPOINTMENTS)
            // -------------------------------------------------------------
            composable(Screen.Appointments.route) {
                AppointmentsScreen(
                    onMenuClick = onClickAbrirDrawer
                )
            }

            // -------------------------------------------------------------
            // 6. RUTA: HISTORIAL MEDICO (MEDICAL HISTORY)
            // -------------------------------------------------------------
            composable(Screen.MedicalHistory.route) {
                MedicalHistoryScreen(
                    onVolverInicio = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // -------------------------------------------------------------
            // 7. RUTA: PERFIL (PROFILE)
            // -------------------------------------------------------------
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onMenuClick = onClickAbrirDrawer
                )
            }
        }
    }
}