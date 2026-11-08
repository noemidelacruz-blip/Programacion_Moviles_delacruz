package com.delacruz.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.catalogo.ui.MainViewModel
import com.delacruz.catalogo.ui.MainViewModelFactory
import com.delacruz.catalogo.ui.UiState
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.components.ErrorView
import com.delacruz.catalogo.ui.detalle.DetalleScreen
import com.delacruz.catalogo.ui.detalle.DetalleViewModelFactory
import com.delacruz.catalogo.ui.favoritos.FavoritosScreen
import com.delacruz.catalogo.ui.favoritos.FavoritosViewModelFactory
import com.delacruz.catalogo.ui.lista.ListaScreen
import com.delacruz.catalogo.ui.lista.ListaViewModelFactory
import com.delacruz.catalogo.ui.login.LoginScreen
import com.delacruz.catalogo.ui.login.LoginViewModelFactory
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
                            PantallaCatalogo(
                                container = container,
                                onCerrarSesion = mainViewModel::cerrarSesion
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Navegación temporal entre lista, favoritos y detalle (hitos H7, H10 y H13).
 * En H14 se reemplaza por AppNav con las pantallas según el rol.
 */
@Composable
private fun PantallaCatalogo(
    container: AppContainer,
    onCerrarSesion: () -> Unit
) {
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var verFavoritos by rememberSaveable { mutableStateOf(false) }

    val id = idSeleccionado
    when {
        id != null -> DetalleScreen(
            viewModel = viewModel(
                key = "detalle-$id",
                factory = DetalleViewModelFactory(container.itemRepository, id)
            ),
            onVolver = { idSeleccionado = null }
        )

        verFavoritos -> FavoritosScreen(
            viewModel = viewModel(
                factory = FavoritosViewModelFactory(container.itemRepository)
            ),
            onVolver = { verFavoritos = false },
            onItemClick = { idSeleccionado = it }
        )

        else -> ListaScreen(
            viewModel = viewModel(
                factory = ListaViewModelFactory(container.itemRepository)
            ),
            onItemClick = { idSeleccionado = it },
            onVerFavoritos = { verFavoritos = true },
            onCerrarSesion = onCerrarSesion
        )
    }
}
