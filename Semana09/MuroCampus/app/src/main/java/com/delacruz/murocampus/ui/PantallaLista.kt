package com.delacruz.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.delacruz.murocampus.model.Post
import com.delacruz.murocampus.viewmodel.UiState

/**
 * Pantalla del muro. Solo dibuja el estado que recibe del ViewModel: nunca
 * llama a la API ni conoce Retrofit.
 */
@Composable
fun PantallaLista(
    estado: UiState<List<Post>>,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    onAbrir: (Int) -> Unit = {}
) {
    when (estado) {
        is UiState.Cargando -> Cargando(modifier = modifier)

        is UiState.Error -> ErrorConReintento(
            mensaje = estado.mensaje,
            onReintentar = onReintentar,
            modifier = modifier
        )

        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
