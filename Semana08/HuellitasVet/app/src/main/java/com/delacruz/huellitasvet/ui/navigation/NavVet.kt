package com.delacruz.huellitasvet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delacruz.huellitasvet.VetApp
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.model.Rol
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.dueno.LoginScreen
import com.delacruz.huellitasvet.ui.dueno.SplashScreen
import com.delacruz.huellitasvet.ui.theme.VetTheme
import com.delacruz.huellitasvet.ui.veterinario.ServiciosScreen
import com.delacruz.huellitasvet.viewmodel.ServiciosViewModel
import com.delacruz.huellitasvet.viewmodel.SesionViewModel
import com.delacruz.huellitasvet.viewmodel.VetViewModelFactory

// Primer nombre del usuario para el saludo (Noemi De La Cruz -> Noemi)
private fun primerNombre(usuario: Usuario?): String =
    usuario?.nombre?.substringBefore(" ") ?: ""

// Navegacion de la app: Splash -> Login -> pantalla principal del perfil.
// Cada destino aplica el tema de su perfil (esmeralda para el dueno, azul para el veterinario)
// y el modo oscuro guardado. En el commit 14 se agrega el grafo completo de cada perfil
@Composable
fun NavVet() {
    val navController = rememberNavController()

    // El repositorio y las preferencias viven en VetApp; la fabrica los entrega a los ViewModels
    val app = LocalContext.current.applicationContext as VetApp
    val fabrica = remember { VetViewModelFactory(app.repository, app.preferencias) }

    // Un solo SesionViewModel para toda la app: guarda al usuario con sesion iniciada
    val sesionVm: SesionViewModel = viewModel(factory = fabrica)
    val modoOscuro by sesionVm.modoOscuro.collectAsState()

    // Va a la pantalla principal del perfil y limpia la pila para que "atras" no vuelva al login
    val irAPrincipal: (Usuario) -> Unit = { usuario ->
        val destino = if (Rol.desdeTexto(usuario.rol) == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO
        navController.navigate(destino) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }
    val irALogin: () -> Unit = { navController.navigate(Rutas.LOGIN) }
    val cerrarSesion: () -> Unit = {
        sesionVm.cerrarSesion()
        navController.navigate(Rutas.LOGIN) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            val cargando by sesionVm.cargando.collectAsState()
            // Si ya hay una sesion guardada, salta el login y va directo a su pantalla principal
            LaunchedEffect(usuario, cargando) {
                val actual = usuario
                if (!cargando && actual != null) irAPrincipal(actual)
            }
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                SplashScreen(onComenzar = irALogin, onYaTengoCuenta = irALogin)
            }
        }
        composable(Rutas.LOGIN) {
            // El login aplica su propio tema, porque cambia de color segun la pestana elegida
            LoginScreen(vm = sesionVm, onLoginExitoso = irAPrincipal)
        }
        composable(Rutas.INICIO) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                // Provisional: se reemplaza por InicioScreen en el commit 8
                PantallaTemporal(
                    titulo = "Inicio del dueño",
                    saludo = "Hola, ${primerNombre(usuario)}",
                    onCerrarSesion = cerrarSesion
                )
            }
        }
        composable(Rutas.PANEL) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                // Provisional: se reemplaza por PanelScreen en el commit 12
                PantallaTemporal(
                    titulo = "Panel de la clínica",
                    saludo = "Hola, ${primerNombre(usuario)}",
                    onCerrarSesion = cerrarSesion,
                    accionExtra = {
                        VetButton(
                            texto = "Ver servicios",
                            onClick = { navController.navigate(Rutas.SERVICIOS) }
                        )
                    }
                )
            }
        }
        composable(Rutas.SERVICIOS) {
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                val vm: ServiciosViewModel = viewModel(factory = fabrica)
                // Editar y nuevo se conectan con el formulario en el commit 14
                ServiciosScreen(vm = vm, onEditar = {}, onNuevo = {})
            }
        }
    }
}