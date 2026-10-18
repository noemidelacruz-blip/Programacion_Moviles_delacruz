package com.delacruz.huellitasvet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.delacruz.huellitasvet.VetApp
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.model.Rol
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.dueno.FichaMascotaScreen
import com.delacruz.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.delacruz.huellitasvet.ui.dueno.InicioScreen
import com.delacruz.huellitasvet.ui.dueno.LoginScreen
import com.delacruz.huellitasvet.ui.dueno.MascotasScreen
import com.delacruz.huellitasvet.ui.dueno.SplashScreen
import com.delacruz.huellitasvet.ui.theme.VetTheme
import com.delacruz.huellitasvet.ui.veterinario.ServiciosScreen
import com.delacruz.huellitasvet.viewmodel.InicioViewModel
import com.delacruz.huellitasvet.viewmodel.MascotaViewModel
import com.delacruz.huellitasvet.viewmodel.ServiciosViewModel
import com.delacruz.huellitasvet.viewmodel.SesionViewModel
import com.delacruz.huellitasvet.viewmodel.VetViewModelFactory

// Primer nombre del usuario para el saludo (Noemi De La Cruz -> Noemi)
private fun primerNombre(usuario: Usuario?): String =
    usuario?.nombre?.substringBefore(" ") ?: ""

// Navegacion de la app: Splash -> Login -> pantalla principal del perfil.
// El dueno tiene barra inferior con Inicio, Mascotas, Citas y Perfil, y puede abrir la ficha
// de una mascota y el formulario de registro o edicion.
// Cada destino aplica el tema de su perfil y el modo oscuro guardado.
// En el commit 14 se completa el grafo de cada perfil
@Composable
fun NavVet() {
    val navController = rememberNavController()

    // El repositorio y las preferencias viven en VetApp; la fabrica los entrega a los ViewModels
    val app = LocalContext.current.applicationContext as VetApp
    val fabrica = remember { VetViewModelFactory(app.repository, app.preferencias) }
    // Fabrica para los ViewModels que necesitan saber que dueno tiene la sesion iniciada
    val fabricaDe: (Int) -> VetViewModelFactory =
        { id -> VetViewModelFactory(app.repository, app.preferencias, id) }

    // Un solo SesionViewModel para toda la app: guarda al usuario con sesion iniciada
    val sesionVm: SesionViewModel = viewModel(factory = fabrica)
    val modoOscuro by sesionVm.modoOscuro.collectAsState()

    // Ruta que se esta mostrando (para marcar el item activo de la barra inferior)
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

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

    // Cambia de pestana en la barra inferior conservando el estado de cada una
    val irAPestana: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.INICIO) { saveState = true }
            launchSingleTop = true
            restoreState = true
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

        // ---------- Perfil del dueno (con barra inferior) ----------
        composable(Rutas.INICIO) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            val actual = usuario
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                if (actual != null) {
                    // La clave por usuario evita mostrar los datos de otra cuenta al cambiar de sesion
                    val fabricaDueno = remember(actual.id) { fabricaDe(actual.id) }
                    val vm: InicioViewModel = viewModel(key = "dueno-${actual.id}", factory = fabricaDueno)
                    ConBarraDueno(rutaActual = rutaActual, onIr = irAPestana) { modifier ->
                        InicioScreen(
                            vm = vm,
                            nombre = primerNombre(actual),
                            onVerMascotas = { irAPestana(Rutas.MASCOTAS) },
                            onMascota = { navController.navigate(Rutas.ficha(it)) },
                            onAgregar = { navController.navigate(Rutas.formulario()) },
                            // Pedir cita se conecta en el commit 10
                            onPedirCita = {},
                            modifier = modifier
                        )
                    }
                }
            }
        }
        composable(Rutas.MASCOTAS) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            val actual = usuario
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                if (actual != null) {
                    val fabricaDueno = remember(actual.id) { fabricaDe(actual.id) }
                    val vm: InicioViewModel = viewModel(key = "dueno-${actual.id}", factory = fabricaDueno)
                    ConBarraDueno(rutaActual = rutaActual, onIr = irAPestana) { modifier ->
                        MascotasScreen(
                            vm = vm,
                            onMascota = { navController.navigate(Rutas.ficha(it)) },
                            onAgregar = { navController.navigate(Rutas.formulario()) },
                            modifier = modifier
                        )
                    }
                }
            }
        }
        composable(Rutas.CITAS) {
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                ConBarraDueno(rutaActual = rutaActual, onIr = irAPestana) {
                    // Provisional: se reemplaza por MisCitasScreen en el commit 11
                    PantallaTemporal(
                        titulo = "Mis citas",
                        saludo = "",
                        onCerrarSesion = cerrarSesion
                    )
                }
            }
        }
        composable(Rutas.PERFIL) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                ConBarraDueno(rutaActual = rutaActual, onIr = irAPestana) {
                    // Provisional: se reemplaza por PerfilScreen en el commit 13
                    PantallaTemporal(
                        titulo = "Perfil",
                        saludo = usuario?.nombre ?: "",
                        onCerrarSesion = cerrarSesion
                    )
                }
            }
        }

        // ---------- Ficha y formulario de mascota ----------
        composable(
            route = Rutas.FICHA,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val mascotaId = entrada.arguments?.getInt("id") ?: 0
            val usuario by sesionVm.usuarioActual.collectAsState()
            val actual = usuario
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                if (actual != null) {
                    val fabricaDueno = remember(actual.id) { fabricaDe(actual.id) }
                    val vm: MascotaViewModel = viewModel(factory = fabricaDueno)
                    // La barra inferior marca "Mascotas", como en el mockup
                    ConBarraDueno(rutaActual = Rutas.MASCOTAS, onIr = irAPestana) { modifier ->
                        FichaMascotaScreen(
                            vm = vm,
                            mascotaId = mascotaId,
                            onVolver = { navController.popBackStack() },
                            // Pedir cita se conecta en el commit 10
                            onPedirCita = {},
                            onEditar = { navController.navigate(Rutas.formulario(mascotaId)) },
                            modifier = modifier
                        )
                    }
                }
            }
        }
        composable(
            route = Rutas.FORMULARIO,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { entrada ->
            val mascotaId = entrada.arguments?.getInt("id") ?: 0
            val usuario by sesionVm.usuarioActual.collectAsState()
            val actual = usuario
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                if (actual != null) {
                    val fabricaDueno = remember(actual.id) { fabricaDe(actual.id) }
                    val vm: MascotaViewModel = viewModel(factory = fabricaDueno)
                    FormularioMascotaScreen(
                        vm = vm,
                        mascotaId = mascotaId,
                        onVolver = { navController.popBackStack() },
                        // Al eliminar, se vuelve a la pantalla anterior a la ficha
                        onEliminada = { navController.popBackStack(Rutas.FICHA, inclusive = true) }
                    )
                }
            }
        }

        // ---------- Perfil del veterinario ----------
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