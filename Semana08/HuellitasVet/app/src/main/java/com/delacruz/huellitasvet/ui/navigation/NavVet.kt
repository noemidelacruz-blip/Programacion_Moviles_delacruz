package com.delacruz.huellitasvet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delacruz.huellitasvet.VetApp
import com.delacruz.huellitasvet.model.Rol
import com.delacruz.huellitasvet.ui.dueno.SplashScreen
import com.delacruz.huellitasvet.ui.theme.VetTheme
import com.delacruz.huellitasvet.ui.veterinario.ServiciosScreen
import com.delacruz.huellitasvet.viewmodel.ServiciosViewModel
import com.delacruz.huellitasvet.viewmodel.VetViewModelFactory

// Navegacion de la app. Por ahora: Splash -> Servicios.
// Cada destino aplica el tema de su perfil (esmeralda para el dueno, azul para el veterinario).
// En los commits 7 y 14 se agregan el login y el grafo completo de cada perfil
@Composable
fun NavVet() {
    val navController = rememberNavController()

    // El repositorio vive en VetApp; la fabrica lo entrega a los ViewModels
    val app = LocalContext.current.applicationContext as VetApp
    val fabrica = remember { VetViewModelFactory(app.repository) }

    // Va a Servicios y borra el Splash de la pila para que "atras" no vuelva a el
    val irAServicios: () -> Unit = {
        navController.navigate(Rutas.SERVICIOS) {
            popUpTo(Rutas.SPLASH) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            VetTheme(rol = Rol.CLIENTE) {
                SplashScreen(
                    onComenzar = irAServicios,
                    onYaTengoCuenta = irAServicios
                )
            }
        }
        composable(Rutas.SERVICIOS) {
            VetTheme(rol = Rol.ADMIN) {
                val vm: ServiciosViewModel = viewModel(factory = fabrica)
                // Editar y nuevo se conectan con el formulario en el commit 14
                ServiciosScreen(vm = vm, onEditar = {}, onNuevo = {})
            }
        }
    }
}