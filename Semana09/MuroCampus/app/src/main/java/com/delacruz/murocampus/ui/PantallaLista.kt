package com.delacruz.murocampus.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.murocampus.model.Post
import com.delacruz.murocampus.ui.theme.FormaPildora
import com.delacruz.murocampus.ui.theme.MoradoClaro
import com.delacruz.murocampus.ui.theme.MoradoPrincipal
import com.delacruz.murocampus.ui.theme.SuperficieBlanca
import com.delacruz.murocampus.ui.theme.TextoCuerpo
import com.delacruz.murocampus.viewmodel.UiState

/**
 * Pantalla del muro. Solo dibuja el estado que recibe del ViewModel: nunca
 * llama a la API ni conoce Retrofit. El buscador también vive en el ViewModel:
 * aquí únicamente se muestra su texto y se avisa cada cambio.
 */
@Composable
fun PantallaLista(
    estado: UiState<List<Post>>,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    usuarioFiltro: Int? = null,
    onFiltrar: (Int?) -> Unit = {},
    busqueda: String = "",
    onBuscar: (String) -> Unit = {},
    onAbrir: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            // Deja ver los resultados cuando el teclado está abierto.
            .imePadding()
    ) {
        CabeceraMuro()

        CampoBusqueda(
            texto = busqueda,
            onBuscar = onBuscar
        )

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
                    SinResultados(busqueda = busqueda)
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (busqueda.isNotBlank()) {
                            Text(
                                text = when (estado.datos.size) {
                                    1 -> "1 coincidencia"
                                    else -> "${estado.datos.size} coincidencias"
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = MoradoPrincipal,
                                modifier = Modifier.padding(
                                    start = 20.dp,
                                    end = 20.dp,
                                    top = 2.dp,
                                    bottom = 6.dp
                                )
                            )
                        }
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
}

/** Campo de búsqueda por título, con lupa y aspa para vaciarlo. */
@Composable
private fun CampoBusqueda(
    texto: String,
    onBuscar: (String) -> Unit
) {
    OutlinedTextField(
        value = texto,
        onValueChange = onBuscar,
        singleLine = true,
        placeholder = { Text("Buscar por título") },
        leadingIcon = { IconoBuscar(color = MoradoPrincipal, tamano = 20.dp) },
        trailingIcon = if (texto.isEmpty()) {
            null
        } else {
            {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onBuscar("") },
                    contentAlignment = Alignment.Center
                ) {
                    IconoCerrar(color = TextoCuerpo, tamano = 16.dp)
                }
            }
        },
        shape = FormaPildora,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextoCuerpo,
            unfocusedTextColor = TextoCuerpo,
            focusedContainerColor = SuperficieBlanca,
            unfocusedContainerColor = SuperficieBlanca,
            cursorColor = MoradoPrincipal,
            focusedBorderColor = MoradoPrincipal,
            unfocusedBorderColor = MoradoClaro,
            focusedPlaceholderColor = TextoCuerpo,
            unfocusedPlaceholderColor = TextoCuerpo
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 10.dp)
    )
}

/** Aviso cuando la lista filtrada queda vacía. */
@Composable
private fun SinResultados(busqueda: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (busqueda.isBlank()) {
                "Sin publicaciones"
            } else {
                "Sin resultados para “$busqueda”"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = TextoCuerpo,
            textAlign = TextAlign.Center
        )
        if (busqueda.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Vuelve a escribir o toca la X para ver todo el muro.",
                style = MaterialTheme.typography.bodySmall,
                color = TextoCuerpo,
                textAlign = TextAlign.Center
            )
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
