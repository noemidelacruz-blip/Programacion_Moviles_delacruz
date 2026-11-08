package com.delacruz.catalogo.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.delacruz.catalogo.ui.components.ErrorView
import com.delacruz.catalogo.ui.components.TarjetaProducto

/**
 * Pantalla de lista (hitos H4 a H10).
 *
 * La pantalla solo observa el estado del ViewModel y llama a sus funciones.
 * No usa Retrofit, Room ni Firebase.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit,
    onVerFavoritos: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "BeautyEmi · Productos") },
                actions = {
                    TextButton(
                        onClick = onVerFavoritos,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Favoritos")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            aviso?.let { texto ->
                AvisoSinConexion(
                    texto = texto,
                    onReintentar = viewModel::refrescar
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                when (val resultado = estado) {
                    is UiState.Cargando -> Cargando()

                    is UiState.Error -> ErrorView(
                        mensaje = resultado.mensaje,
                        onReintentar = viewModel::refrescar
                    )

                    is UiState.Exito -> {
                        if (resultado.datos.isEmpty()) {
                            ListaVacia()
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
}

/** Aviso que aparece cuando se muestran datos guardados sin internet. */
@Composable
private fun AvisoSinConexion(
    texto: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚠ $texto",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = onReintentar,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "Reintentar")
            }
        }
    }
}

/** Estado vacío: la consulta terminó pero no hay productos. */
@Composable
private fun ListaVacia(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No hay productos para mostrar",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Presiona Reintentar para volver a cargar el catálogo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
