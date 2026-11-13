package com.delacruz.catalogo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.catalogo.AppContainer
import com.delacruz.catalogo.ui.bienvenida.BienvenidaScreen
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.components.ErrorView
import com.delacruz.catalogo.ui.components.FondoBeautyEmi
import com.delacruz.catalogo.ui.login.LoginScreen
import com.delacruz.catalogo.ui.login.LoginViewModelFactory
import com.delacruz.catalogo.ui.navigation.AppNav

/**
 * Composable raíz: decide qué se muestra según la sesión.
 *
 * Cargando -> comprobando sesión | Error -> mensaje |
 * Sin sesión -> Bienvenida / Login | Con sesión -> AppNav (el rol decide
 * las pantallas).
 */
@Composable
fun CatalogoAppContent(container: AppContainer) {
    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(container.authRepository)
    )
    val estadoSesion by mainViewModel.estado.collectAsStateWithLifecycle()

    // Al arrancar sin sesión se muestra la bienvenida. Luego de tocar
    // "Iniciar sesión" se va al login y, al cerrar sesión, se vuelve
    // directo al login (PA-12).
    var mostrarBienvenida by rememberSaveable { mutableStateOf(true) }

    when (val resultado = estadoSesion) {
        is UiState.Cargando -> FondoBeautyEmi {
            Cargando(mensaje = "Comprobando sesión...")
        }

        is UiState.Error -> FondoBeautyEmi {
            ErrorView(
                mensaje = resultado.mensaje,
                onReintentar = {}
            )
        }

        is UiState.Exito -> {
            val sesion = resultado.datos
            when {
                sesion != null -> AppNav(
                    container = container,
                    sesion = sesion,
                    onCerrarSesion = mainViewModel::cerrarSesion
                )

                mostrarBienvenida -> BienvenidaScreen(
                    onIniciarSesion = { mostrarBienvenida = false }
                )

                else -> LoginScreen(
                    viewModel = viewModel(
                        factory = LoginViewModelFactory(container.authRepository)
                    ),
                    onLoginExitoso = mainViewModel::actualizarSesion,
                    onVolver = { mostrarBienvenida = true }
                )
            }
        }
    }
}