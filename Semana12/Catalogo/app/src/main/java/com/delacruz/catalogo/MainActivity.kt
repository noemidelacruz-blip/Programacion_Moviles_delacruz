package com.delacruz.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.catalogo.ui.MainViewModel
import com.delacruz.catalogo.ui.MainViewModelFactory
import com.delacruz.catalogo.ui.UiState
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.components.ErrorView
import com.delacruz.catalogo.ui.login.LoginScreen
import com.delacruz.catalogo.ui.login.LoginViewModelFactory
import com.delacruz.catalogo.ui.navigation.AppNav
import com.delacruz.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CatalogoApp).container

        setContent {
            CatalogoTheme {
                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(container.authRepository)
                )
                val estadoSesion by mainViewModel.estado.collectAsStateWithLifecycle()

                when (val resultado = estadoSesion) {
                    is UiState.Cargando -> Cargando(mensaje = "Comprobando sesión...")

                    is UiState.Error -> ErrorView(
                        mensaje = resultado.mensaje,
                        onReintentar = {}
                    )

                    is UiState.Exito -> {
                        val sesion = resultado.datos
                        if (sesion == null) {
                            LoginScreen(
                                viewModel = viewModel(
                                    factory = LoginViewModelFactory(container.authRepository)
                                ),
                                onLoginExitoso = mainViewModel::actualizarSesion
                            )
                        } else {
                            // El rol leído de Firestore decide qué pantallas hay.
                            AppNav(
                                container = container,
                                sesion = sesion,
                                onCerrarSesion = mainViewModel::cerrarSesion
                            )
                        }
                    }
                }
            }
        }
    }
}
