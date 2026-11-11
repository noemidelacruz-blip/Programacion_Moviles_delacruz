package com.delacruz.catalogo.ui.detalle

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.delacruz.catalogo.R
import com.delacruz.catalogo.domain.model.Item
import com.delacruz.catalogo.ui.UiState
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.components.ErrorView
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.ChocolateText
import com.delacruz.catalogo.ui.theme.CoralAccent
import com.delacruz.catalogo.ui.theme.CreamSurface
import com.delacruz.catalogo.ui.theme.ItemImageBackground
import com.delacruz.catalogo.ui.theme.MutedBrown
import com.delacruz.catalogo.ui.theme.PeachSoft
import com.delacruz.catalogo.ui.theme.StockGreen
import java.util.Locale

/**
 * Pantalla de detalle (hito H7).
 *
 * Muestra el producto pedido al segundo endpoint (/products/{id}).
 * La pantalla solo observa el estado y llama al ViewModel. El fondo con
 * degradado y ondas lo aporta AppNav.
 */
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val favorito by viewModel.favorito.collectAsStateWithLifecycle()

    // El botón atrás del sistema también vuelve a la lista.
    BackHandler(onBack = onVolver)

    Box(modifier = modifier.fillMaxSize()) {
        when (val resultado = estado) {
            is UiState.Cargando -> Cargando(mensaje = "Cargando detalle...")

            is UiState.Error -> ErrorView(
                mensaje = resultado.mensaje,
                onReintentar = viewModel::cargar
            )

            is UiState.Exito -> DetalleContenido(
                item = resultado.datos,
                favorito = favorito,
                onAlternarFavorito = viewModel::alternarFavorito
            )
        }

        // Botón de retroceso flotante sobre la imagen.
        Surface(
            shape = CircleShape,
            color = CreamSurface,
            shadowElevation = 3.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 16.dp, top = 10.dp)
                .size(44.dp)
        ) {
            IconButton(onClick = onVolver, modifier = Modifier.size(44.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_flecha_izquierda),
                    contentDescription = "Volver",
                    tint = Chocolate,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/** Contenido del detalle ya cargado. */
@Composable
private fun DetalleContenido(
    item: Item,
    favorito: Boolean,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(58.dp))

        // Imagen del producto en una tarjeta durazno.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(300.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(ItemImageBackground)
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            )

            Surface(
                shape = CircleShape,
                color = CreamSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(44.dp)
            ) {
                IconButton(onClick = onAlternarFavorito, modifier = Modifier.size(44.dp)) {
                    Icon(
                        painter = painterResource(
                            if (favorito) R.drawable.ic_corazon else R.drawable.ic_corazon_borde
                        ),
                        contentDescription = if (favorito) {
                            "Quitar de favoritos"
                        } else {
                            "Agregar a favoritos"
                        },
                        tint = if (favorito) CoralAccent else MutedBrown,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = PeachSoft
                ) {
                    Text(
                        text = item.categoria.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelMedium,
                        color = Chocolate,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(R.drawable.ic_estrella),
                    contentDescription = null,
                    tint = CoralAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format(Locale.getDefault(), "%.1f", item.calificacion),
                    style = MaterialTheme.typography.labelLarge,
                    color = ChocolateText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.titulo,
                style = MaterialTheme.typography.headlineMedium,
                color = ChocolateText
            )
            Text(
                text = item.marca,
                style = MaterialTheme.typography.titleMedium,
                color = CoralAccent,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format(Locale.getDefault(), "S/ %.2f", item.precio),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Chocolate
                )
                Spacer(modifier = Modifier.width(14.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(
                                if (item.stock > 0) StockGreen else MutedBrown,
                                CircleShape
                            )
                    )
                    Text(
                        text = if (item.stock > 0) "${item.stock} en stock" else "Agotado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onAlternarFavorito,
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (favorito) CoralAccent else Chocolate,
                    contentColor = CreamSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (favorito) R.drawable.ic_corazon else R.drawable.ic_corazon_borde
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (favorito) "Quitar de favoritos" else "Agregar a favoritos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleLarge,
                color = ChocolateText
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = CreamSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = item.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ChocolateText,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
