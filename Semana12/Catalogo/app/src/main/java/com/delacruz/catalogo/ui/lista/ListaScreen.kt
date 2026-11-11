package com.delacruz.catalogo.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.delacruz.catalogo.R
import com.delacruz.catalogo.ui.UiState
import com.delacruz.catalogo.ui.components.BarraSuperior
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.components.ErrorView
import com.delacruz.catalogo.ui.components.TarjetaProducto
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.ChocolateText
import com.delacruz.catalogo.ui.theme.CoralAccent
import com.delacruz.catalogo.ui.theme.CreamSurface
import com.delacruz.catalogo.ui.theme.MutedBrown
import com.delacruz.catalogo.ui.theme.PeachSoft

/**
 * Pantalla de lista o catálogo (hitos H4 a H14).
 *
 * La pantalla solo observa el estado del ViewModel y llama a sus funciones.
 * No usa Retrofit, Room ni Firebase. La navegación entre pestañas la maneja
 * AppNav con la barra inferior.
 */
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit,
    onAbrirFavoritos: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(12.dp))

            BarraSuperior(
                onCerrarSesion = onCerrarSesion,
                onAbrirFavoritos = onAbrirFavoritos
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Catálogo Beauty",
                style = MaterialTheme.typography.headlineMedium,
                color = ChocolateText
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Encuentra tu nuevo favorito de belleza",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedBrown
            )

            aviso?.let { texto ->
                Spacer(modifier = Modifier.height(14.dp))
                AvisoSinConexion(
                    texto = texto,
                    onReintentar = viewModel::refrescar
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
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
                        ListaVacia(onActualizar = viewModel::refrescar)
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

/** Aviso que aparece cuando se muestran datos guardados sin internet. */
@Composable
private fun AvisoSinConexion(
    texto: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CreamSurface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(PeachSoft, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_wifi_off),
                    contentDescription = null,
                    tint = CoralAccent,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.bodySmall,
                color = ChocolateText,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onReintentar) {
                Text(
                    text = "Reintentar",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Chocolate
                )
            }
        }
    }
}

/** Estado vacío: la consulta terminó pero no hay productos. */
@Composable
private fun ListaVacia(
    onActualizar: () -> Unit,
    modifier: Modifier = Modifier
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
                painter = painterResource(R.drawable.ic_bolsa),
                contentDescription = null,
                tint = CoralAccent,
                modifier = Modifier.size(38.dp)
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Tu catálogo está vacío",
            style = MaterialTheme.typography.titleLarge,
            color = ChocolateText,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Sincroniza para descargar los productos de belleza.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedBrown,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )
        Button(
            onClick = onActualizar,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Sincronizar")
        }
    }
}
