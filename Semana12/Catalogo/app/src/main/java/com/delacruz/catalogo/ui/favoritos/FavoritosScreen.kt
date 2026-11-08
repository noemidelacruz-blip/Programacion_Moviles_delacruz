package com.delacruz.catalogo.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.delacruz.catalogo.ui.UiState
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.components.TarjetaProducto

/**
 * Pantalla de favoritos (hito H10).
 *
 * Muestra los productos marcados con el corazón. Como la lista viene de Room,
 * se conserva al cerrar la app y al sincronizar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onVolver: () -> Unit,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Favoritos") },
                navigationIcon = {
                    TextButton(
                        onClick = onVolver,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val resultado = estado) {
                is UiState.Cargando -> Cargando(mensaje = "Cargando favoritos...")

                is UiState.Error -> SinFavoritos(mensaje = resultado.mensaje)

                is UiState.Exito -> {
                    if (resultado.datos.isEmpty()) {
                        SinFavoritos()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(resultado.datos, key = { it.id }) { item ->
                                TarjetaProducto(
                                    item = item,
                                    onClick = { onItemClick(item.id) },
                                    onAlternarFavorito = { viewModel.alternarFavorito(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Mensaje cuando todavía no hay favoritos. */
@Composable
private fun SinFavoritos(
    modifier: Modifier = Modifier,
    mensaje: String = "Todavía no tienes favoritos.\nToca el corazón de un producto para agregarlo aquí."
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}
