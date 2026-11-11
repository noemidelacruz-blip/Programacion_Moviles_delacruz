package com.delacruz.catalogo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.catalogo.ui.MainViewModel
import com.delacruz.catalogo.ui.MainViewModelFactory
import com.delacruz.catalogo.ui.UiState
import com.delacruz.catalogo.ui.bienvenida.BienvenidaScreen
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.components.ErrorView
import com.delacruz.catalogo.ui.components.FondoBeautyEmi
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
                // R09: se pide el permiso de notificaciones una sola vez.
                // Si la persona lo niega, la app sigue funcionando (PA-14).
                val contexto = LocalContext.current
                val permisoNotificaciones = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { _ -> }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(
                            contexto,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(container.authRepository)
                )
                val estadoSesion by mainViewModel.estado.collectAsStateWithLifecycle()

                // Al arrancar sin sesión se muestra la bienvenida. Luego de
                // tocar "Iniciar sesión" se va al login y, al cerrar sesión,
                // se vuelve directo al login (PA-12).
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
                        if (sesion == null) {
                            if (mostrarBienvenida) {
                                BienvenidaScreen(
                                    onIniciarSesion = { mostrarBienvenida = false }
                                )
                            } else {
                                LoginScreen(
                                    viewModel = viewModel(
                                        factory = LoginViewModelFactory(container.authRepository)
                                    ),
                                    onLoginExitoso = mainViewModel::actualizarSesion,
                                    onVolver = { mostrarBienvenida = true }
                                )
                            }
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
