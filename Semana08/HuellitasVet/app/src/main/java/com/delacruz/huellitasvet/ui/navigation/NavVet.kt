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
import com.delacruz.huellitasvet.ui.dueno.FichaMascotaScreen
import com.delacruz.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.delacruz.huellitasvet.ui.dueno.InicioScreen
import com.delacruz.huellitasvet.ui.dueno.LoginScreen
import com.delacruz.huellitasvet.ui.dueno.MascotasScreen
import com.delacruz.huellitasvet.ui.dueno.MisCitasScreen
import com.delacruz.huellitasvet.ui.dueno.PedirCitaScreen
import com.delacruz.huellitasvet.ui.dueno.PerfilScreen
import com.delacruz.huellitasvet.ui.dueno.SplashScreen
import com.delacruz.huellitasvet.ui.theme.VetTheme
import com.delacruz.huellitasvet.ui.veterinario.AgendaScreen
import com.delacruz.huellitasvet.ui.veterinario.AjustesScreen
import com.delacruz.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.delacruz.huellitasvet.ui.veterinario.FormularioServicioScreen
import com.delacruz.huellitasvet.ui.veterinario.PacientesScreen
import com.delacruz.huellitasvet.ui.veterinario.PanelScreen
import com.delacruz.huellitasvet.ui.veterinario.ServiciosScreen
import com.delacruz.huellitasvet.viewmodel.AgendaViewModel
import com.delacruz.huellitasvet.viewmodel.AtenderCitaViewModel
import com.delacruz.huellitasvet.viewmodel.InicioViewModel
import com.delacruz.huellitasvet.viewmodel.MascotaViewModel
import com.delacruz.huellitasvet.viewmodel.MisCitasViewModel
import com.delacruz.huellitasvet.viewmodel.PacientesViewModel
import com.delacruz.huellitasvet.viewmodel.PanelViewModel
import com.delacruz.huellitasvet.viewmodel.PedirCitaViewModel
import com.delacruz.huellitasvet.viewmodel.PerfilViewModel
import com.delacruz.huellitasvet.viewmodel.ServiciosViewModel
import com.delacruz.huellitasvet.viewmodel.SesionViewModel
import com.delacruz.huellitasvet.viewmodel.VetViewModelFactory

// Primer nombre del usuario para el saludo (Noemi De La Cruz -> Noemi)
private fun primerNombre(usuario: Usuario?): String =
    usuario?.nombre?.substringBefore(" ") ?: ""

// Navegacion de la app: Splash -> Login -> pantalla principal del perfil.
// El dueno tiene barra inferior con Inicio, Mascotas, Citas y Perfil, y el veterinario con
// Panel, Agenda, Pacientes, Servicios y Ajustes. Desde Perfil o Ajustes se puede cambiar de perfil.
// Cada destino aplica el tema de su perfil y el modo oscuro guardado
@Composable
fun NavVet() {
    val navController = rememberNavController()

    // El repositorio y las preferencias viven en VetApp; la fabrica los entrega a los ViewModels
    val app = LocalContext.current.applicationContext as VetApp
    val fabrica = remember { VetViewModelFactory(app.repository, app.preferencias) }
    // Fabrica para los ViewModels que necesitan saber que dueno tiene la sesion iniciada
    val fabricaDe: (Int) -> VetViewModelFactory =
        { id -> VetViewModelFactory(app.repository, app.preferencias, id) }

    // Un solo SesionViewModel para toda la app: guarda al usuario con sesion iniciada y los ajustes
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
    // Cambia al otro perfil: el ViewModel guarda el perfil en DataStore y la navegacion
    // va a su pantalla principal limpiando la pila (popUpTo)
    val cambiarPerfil: () -> Unit = {
        sesionVm.cambiarPerfil { usuario -> irAPrincipal(usuario) }
    }

    // Cambia de pestana en la barra inferior del dueno conservando el estado de cada una
    val irAPestana: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.INICIO) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Lo mismo para la barra inferior del veterinario, cuya pantalla de inicio es el Panel
    val irAPestanaVet: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.PANEL) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Al confirmar una cita se vuelve a la pestana Mis citas. No se usa irAPestana porque
    // este lleva saveState/restoreState: si Mis citas ya estaba debajo de Pedir cita, al
    // restaurar se volveria a poner Pedir cita encima y la pantalla quedaria congelada.
    // Si Mis citas esta en la pila (se llego desde su boton +), se vuelve a ella; si no
    // (se llego desde la ficha de la mascota), se navega quitando Pedir cita.
    val irACitas: () -> Unit = {
        if (!navController.popBackStack(Rutas.CITAS, inclusive = false)) {
            navController.navigate(Rutas.CITAS) {
                popUpTo(Rutas.INICIO) { inclusive = false }
                launchSingleTop = true
            }
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
                            onPedirCita = { navController.navigate(Rutas.pedirCita()) },
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
            val usuario by sesionVm.usuarioActual.collectAsState()
            val actual = usuario
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                if (actual != null) {
                    val fabricaDueno = remember(actual.id) { fabricaDe(actual.id) }
                    val vm: MisCitasViewModel = viewModel(key = "citas-${actual.id}", factory = fabricaDueno)
                    ConBarraDueno(rutaActual = rutaActual, onIr = irAPestana) { modifier ->
                        MisCitasScreen(
                            vm = vm,
                            onVolver = { irAPestana(Rutas.INICIO) },
                            onPedirCita = { navController.navigate(Rutas.pedirCita()) },
                            modifier = modifier
                        )
                    }
                }
            }
        }
        composable(Rutas.PERFIL) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            val notificaciones by sesionVm.notificaciones.collectAsState()
            val actual = usuario
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                if (actual != null) {
                    val fabricaDueno = remember(actual.id) { fabricaDe(actual.id) }
                    val vm: PerfilViewModel = viewModel(key = "perfil-${actual.id}", factory = fabricaDueno)
                    ConBarraDueno(rutaActual = rutaActual, onIr = irAPestana) { modifier ->
                        PerfilScreen(
                            vm = vm,
                            usuario = actual,
                            modoOscuro = modoOscuro,
                            // En el dueno, Recordatorios usa la preferencia de notificaciones
                            recordatorios = notificaciones,
                            onModoOscuro = { sesionVm.cambiarModoOscuro(it) },
                            onRecordatorios = { sesionVm.cambiarNotificaciones(it) },
                            onCambiarPerfil = cambiarPerfil,
                            onCerrarSesion = cerrarSesion,
                            onVolver = { irAPestana(Rutas.INICIO) },
                            modifier = modifier
                        )
                    }
                }
            }
        }

        // ---------- Ficha, formulario de mascota y Pedir cita ----------
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
                            // Pedir cita se abre con esta mascota ya elegida
                            onPedirCita = { navController.navigate(Rutas.pedirCita(mascotaId)) },
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
        composable(
            route = Rutas.PEDIR_CITA,
            arguments = listOf(
                navArgument("mascotaId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { entrada ->
            val mascotaInicial = entrada.arguments?.getInt("mascotaId") ?: 0
            val usuario by sesionVm.usuarioActual.collectAsState()
            val actual = usuario
            VetTheme(rol = Rol.CLIENTE, modoOscuro = modoOscuro) {
                if (actual != null) {
                    val fabricaCita = remember(actual.id, mascotaInicial) {
                        VetViewModelFactory(app.repository, app.preferencias, actual.id, mascotaInicial)
                    }
                    val vm: PedirCitaViewModel = viewModel(factory = fabricaCita)
                    PedirCitaScreen(
                        vm = vm,
                        onVolver = { navController.popBackStack() },
                        // Al confirmar, se vuelve a la pestana Mis citas
                        onCitaCreada = irACitas
                    )
                }
            }
        }

        // ---------- Perfil del veterinario (con barra inferior) ----------
        composable(Rutas.PANEL) {
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                val vm: PanelViewModel = viewModel(factory = fabrica)
                ConBarraVet(rutaActual = rutaActual, onIr = irAPestanaVet) { modifier ->
                    PanelScreen(
                        vm = vm,
                        // El boton Ver de la alerta lleva a la Agenda
                        onVerAgenda = { irAPestanaVet(Rutas.AGENDA) },
                        modifier = modifier
                    )
                }
            }
        }
        composable(Rutas.AGENDA) {
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                val vm: AgendaViewModel = viewModel(factory = fabrica)
                ConBarraVet(rutaActual = rutaActual, onIr = irAPestanaVet) { modifier ->
                    AgendaScreen(
                        vm = vm,
                        onVolver = { irAPestanaVet(Rutas.PANEL) },
                        onAtender = { navController.navigate(Rutas.atender(it)) },
                        modifier = modifier
                    )
                }
            }
        }
        composable(Rutas.PACIENTES) {
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                val vm: PacientesViewModel = viewModel(factory = fabrica)
                ConBarraVet(rutaActual = rutaActual, onIr = irAPestanaVet) { modifier ->
                    PacientesScreen(
                        vm = vm,
                        onVolver = { irAPestanaVet(Rutas.PANEL) },
                        // Tocar una tarjeta abre la ficha con el historial clinico
                        onPaciente = { navController.navigate(Rutas.fichaVet(it)) },
                        modifier = modifier
                    )
                }
            }
        }
        composable(Rutas.SERVICIOS) {
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                val vm: ServiciosViewModel = viewModel(factory = fabrica)
                ConBarraVet(rutaActual = rutaActual, onIr = irAPestanaVet) { modifier ->
                    ServiciosScreen(
                        vm = vm,
                        onVolver = { irAPestanaVet(Rutas.PANEL) },
                        // El lapiz y el boton + abren el formulario de servicio
                        onEditar = { navController.navigate(Rutas.formularioServicio(it)) },
                        onNuevo = { navController.navigate(Rutas.formularioServicio()) },
                        modifier = modifier
                    )
                }
            }
        }
        composable(Rutas.AJUSTES) {
            val usuario by sesionVm.usuarioActual.collectAsState()
            val notificaciones by sesionVm.notificaciones.collectAsState()
            val aceptarCitas by sesionVm.aceptarCitas.collectAsState()
            val actual = usuario
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                if (actual != null) {
                    ConBarraVet(rutaActual = rutaActual, onIr = irAPestanaVet) { modifier ->
                        AjustesScreen(
                            usuario = actual,
                            modoOscuro = modoOscuro,
                            notificaciones = notificaciones,
                            aceptarCitas = aceptarCitas,
                            onModoOscuro = { sesionVm.cambiarModoOscuro(it) },
                            onNotificaciones = { sesionVm.cambiarNotificaciones(it) },
                            onAceptarCitas = { sesionVm.cambiarAceptarCitas(it) },
                            onCambiarPerfil = cambiarPerfil,
                            onCerrarSesion = cerrarSesion,
                            onVolver = { irAPestanaVet(Rutas.PANEL) },
                            modifier = modifier
                        )
                    }
                }
            }
        }

        // ---------- Atender cita, ficha de un paciente y formulario de servicio (veterinario) ----------
        composable(
            route = Rutas.ATENDER,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val citaId = entrada.arguments?.getInt("id") ?: 0
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                // La cita a atender llega a la fabrica como argumento
                val fabricaAtender = remember(citaId) {
                    VetViewModelFactory(app.repository, app.preferencias, 0, citaId)
                }
                val vm: AtenderCitaViewModel = viewModel(factory = fabricaAtender)
                AtenderCitaScreen(
                    vm = vm,
                    // Al registrar o cancelar, se vuelve a la Agenda
                    onVolver = { navController.popBackStack() }
                )
            }
        }
        composable(
            route = Rutas.FICHA_VET,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val mascotaId = entrada.arguments?.getInt("id") ?: 0
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                val vm: MascotaViewModel = viewModel(factory = fabrica)
                // La barra inferior marca "Pacientes"; la ficha va sin los botones del dueno
                ConBarraVet(rutaActual = Rutas.PACIENTES, onIr = irAPestanaVet) { modifier ->
                    FichaMascotaScreen(
                        vm = vm,
                        mascotaId = mascotaId,
                        onVolver = { navController.popBackStack() },
                        puedeRegistrarVacunas = true,
                        modifier = modifier
                    )
                }
            }
        }
        composable(
            route = Rutas.FORMULARIO_SERVICIO,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { entrada ->
            val servicioId = entrada.arguments?.getInt("id") ?: 0
            VetTheme(rol = Rol.ADMIN, modoOscuro = modoOscuro) {
                val vm: ServiciosViewModel = viewModel(factory = fabrica)
                FormularioServicioScreen(
                    vm = vm,
                    servicioId = servicioId,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }
}