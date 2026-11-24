package com.delacruz.murocampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.murocampus.data.PostRepositoryRemote
import com.delacruz.murocampus.data.remote.RetrofitClient
import com.delacruz.murocampus.model.Post
import com.delacruz.murocampus.ui.PantallaCrear
import com.delacruz.murocampus.ui.PantallaDetalle
import com.delacruz.murocampus.ui.PantallaLista
import com.delacruz.murocampus.ui.theme.MuroCampusTheme
import com.delacruz.murocampus.viewmodel.DetalleUi
import com.delacruz.murocampus.viewmodel.PostsViewModel
import com.delacruz.murocampus.viewmodel.PostsViewModelFactory
import com.delacruz.murocampus.viewmodel.UiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La app siempre usa fondo claro: iconos blancos sobre la barra azul y oscuros
        // sobre la barra de navegación inferior.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        val repository = PostRepositoryRemote(RetrofitClient.api)

        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(
                    factory = PostsViewModelFactory(repository)
                )
                val estadoLista by vm.lista.collectAsStateWithLifecycle()
                val estadoDetalle by vm.detalle.collectAsStateWithLifecycle()
                val usuarioFiltro by vm.usuarioFiltro.collectAsStateWithLifecycle()
                val estadoCreacion by vm.creacion.collectAsStateWithLifecycle()

                MuroCampusApp(
                    estadoLista = estadoLista,
                    onReintentarLista = vm::cargar,
                    estadoDetalle = estadoDetalle,
                    onAbrirPublicacion = vm::abrir,
                    usuarioFiltro = usuarioFiltro,
                    onFiltrar = vm::filtrar,
                    estadoCreacion = estadoCreacion,
                    onPublicar = vm::publicar,
                    onLimpiarCreacion = vm::limpiarCreacion
                )
            }
        }
    }
}

/**
 * Contenedor principal de MuroCampus: barra superior, lista, detalle y creación.
 * La navegación usa dos estados guardables: idSeleccionado para el detalle y
 * creando para el formulario; el botón Atrás del sistema siempre vuelve a la lista.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuroCampusApp(
    estadoLista: UiState<List<Post>>,
    onReintentarLista: () -> Unit,
    estadoDetalle: UiState<DetalleUi>,
    onAbrirPublicacion: (Int) -> Unit,
    usuarioFiltro: Int?,
    onFiltrar: (Int?) -> Unit,
    estadoCreacion: UiState<Post>?,
    onPublicar: (String, String) -> Unit,
    onLimpiarCreacion: () -> Unit
) {
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MuroCampus",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (!creando && idSeleccionado == null) {
                FloatingActionButton(
                    onClick = {
                        onLimpiarCreacion()
                        creando = true
                    }
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                creando -> {
                    BackHandler { creando = false }
                    PantallaCrear(
                        estado = estadoCreacion,
                        onPublicar = onPublicar
                    )
                }

                idSeleccionado == null -> PantallaLista(
                    estado = estadoLista,
                    onReintentar = onReintentarLista,
                    usuarioFiltro = usuarioFiltro,
                    onFiltrar = onFiltrar,
                    onAbrir = { id ->
                        idSeleccionado = id
                        onAbrirPublicacion(id)
                    }
                )

                else -> {
                    BackHandler { idSeleccionado = null }
                    PantallaDetalle(
                        estado = estadoDetalle,
                        onReintentar = {
                            idSeleccionado?.let(onAbrirPublicacion)
                        }
                    )
                }
            }
        }
    }
}
