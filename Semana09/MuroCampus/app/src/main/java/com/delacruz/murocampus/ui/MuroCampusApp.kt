package com.delacruz.murocampus.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.delacruz.murocampus.ui.theme.FondoCrema
import com.delacruz.murocampus.viewmodel.PostsViewModel
import com.delacruz.murocampus.viewmodel.UiState

/**
 * Contenedor de MuroCampus: recoge los estados del ViewModel y decide qué
 * pantalla se dibuja. La navegación usa dos estados guardables: idSeleccionado
 * para el detalle y creando para el formulario; el botón Atrás del sistema
 * siempre vuelve a la lista. Las pantallas solo reciben datos y lambdas, nunca
 * el ViewModel ni la API.
 */
@Composable
fun MuroCampusApp(vm: PostsViewModel) {
    val estadoLista by vm.lista.collectAsStateWithLifecycle()
    val estadoDetalle by vm.detalle.collectAsStateWithLifecycle()
    val usuarioFiltro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val estadoCreacion by vm.creacion.collectAsStateWithLifecycle()
    val estadoEliminacion by vm.eliminacion.collectAsStateWithLifecycle()

    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }

    // Cuando el servidor confirma el DELETE, se vuelve al muro.
    LaunchedEffect(estadoEliminacion) {
        if (estadoEliminacion is UiState.Exito) {
            idSeleccionado = null
            vm.limpiarEliminacion()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FondoCrema,
        // Solo se reserva la barra de navegación: la cabecera se dibuja hasta
        // el borde superior, detrás de la barra de estado.
        contentWindowInsets = WindowInsets.navigationBars,
        floatingActionButton = {
            if (!creando && idSeleccionado == null) {
                BotonAgregar(
                    onClick = {
                        vm.limpiarCreacion()
                        creando = true
                    }
                )
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
                        onPublicar = vm::publicar,
                        onVolver = { creando = false }
                    )
                }

                idSeleccionado == null -> PantallaLista(
                    estado = estadoLista,
                    onReintentar = vm::cargar,
                    usuarioFiltro = usuarioFiltro,
                    onFiltrar = vm::filtrar,
                    onAbrir = { id ->
                        vm.limpiarEliminacion()
                        idSeleccionado = id
                        vm.abrir(id)
                    }
                )

                else -> {
                    BackHandler {
                        idSeleccionado = null
                        vm.limpiarEliminacion()
                    }
                    PantallaDetalle(
                        estado = estadoDetalle,
                        onReintentar = {
                            idSeleccionado?.let(vm::abrir)
                        },
                        onVolver = {
                            idSeleccionado = null
                            vm.limpiarEliminacion()
                        },
                        eliminacion = estadoEliminacion,
                        onEliminar = vm::eliminar
                    )
                }
            }
        }
    }
}
