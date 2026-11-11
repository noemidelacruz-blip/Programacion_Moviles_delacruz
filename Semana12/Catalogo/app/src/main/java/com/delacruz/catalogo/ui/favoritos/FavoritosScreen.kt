package com.delacruz.catalogo.ui.favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.delacruz.catalogo.R
import com.delacruz.catalogo.ui.UiState
import com.delacruz.catalogo.ui.components.BarraSuperior
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.components.TarjetaProducto
import com.delacruz.catalogo.ui.theme.ChocolateText
import com.delacruz.catalogo.ui.theme.CoralAccent
import com.delacruz.catalogo.ui.theme.MutedBrown
import com.delacruz.catalogo.ui.theme.PeachSoft

/**
 * Pantalla de favoritos (hito H10).
 *
 * Muestra los productos marcados con el corazón. Como la lista viene de Room,
 * se conserva al cerrar la app y al sincronizar.
 */
@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(12.dp))

            BarraSuperior(onCerrarSesion = onCerrarSesion)

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Favoritos",
                style = MaterialTheme.typography.headlineMedium,
                color = ChocolateText
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Los productos que más te gustan",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedBrown
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Box(modifier = Modifier.weight(1f)) {
            when (val resultado = estado) {
                is UiState.Cargando -> Cargando(mensaje = "Cargando favoritos...")

                is UiState.Error -> SinFavoritos(mensaje = resultado.mensaje)

                is UiState.Exito -> {
                    if (resultado.datos.isEmpty()) {
                        SinFavoritos()
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                top = 6.dp,
                                end = 20.dp,
                                bottom = 20.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
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
        Box(
            modifier = Modifier
                .size(84.dp)
                .background(PeachSoft, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_corazon_borde),
                contentDescription = null,
                tint = CoralAccent,
                modifier = Modifier.size(38.dp)
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            color = ChocolateText,
            textAlign = TextAlign.Center
        )
    }
}
