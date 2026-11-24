package com.delacruz.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.murocampus.model.Post
import com.delacruz.murocampus.ui.theme.FormaPildora
import com.delacruz.murocampus.ui.theme.MoradoPrincipal
import com.delacruz.murocampus.ui.theme.SuperficieBlanca
import com.delacruz.murocampus.ui.theme.TextoCuerpo
import com.delacruz.murocampus.viewmodel.UiState

/**
 * Pantalla del muro. Solo dibuja el estado que recibe del ViewModel: nunca
 * llama a la API ni conoce Retrofit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    estado: UiState<List<Post>>,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    usuarioFiltro: Int? = null,
    onFiltrar: (Int?) -> Unit = {},
    onAbrir: (Int) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {
        CabeceraMuro()

        FilaFiltros(
            usuarioFiltro = usuarioFiltro,
            onFiltrar = onFiltrar
        )

        Box(modifier = Modifier.weight(1f)) {
            when (estado) {
                is UiState.Cargando -> Cargando()

                is UiState.Error -> ErrorConReintento(
                    mensaje = estado.mensaje,
                    onReintentar = onReintentar
                )

                is UiState.Exito -> if (estado.datos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sin publicaciones",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoCuerpo
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 4.dp,
                            bottom = 96.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = estado.datos, key = { post -> post.id }) { post ->
                            TarjetaPost(
                                post = post,
                                onClick = { onAbrir(post.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Fila desplazable de chips: Todos y Usuario 1 a Usuario 10. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilaFiltros(
    usuarioFiltro: Int?,
    onFiltrar: (Int?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            ChipFiltro(
                texto = "Todos",
                seleccionado = usuarioFiltro == null,
                onClick = { onFiltrar(null) }
            )
        }
        items(items = (1..10).toList()) { userId ->
            ChipFiltro(
                texto = "Usuario $userId",
                seleccionado = usuarioFiltro == userId,
                onClick = { onFiltrar(userId) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipFiltro(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        shape = FormaPildora,
        label = {
            Text(
                text = texto,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = SuperficieBlanca,
            labelColor = MoradoPrincipal,
            selectedContainerColor = MoradoPrincipal,
            selectedLabelColor = Color.White
        )
    )
}
