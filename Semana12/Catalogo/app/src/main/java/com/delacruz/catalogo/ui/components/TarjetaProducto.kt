package com.delacruz.catalogo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.delacruz.catalogo.R
import com.delacruz.catalogo.domain.model.Item
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.ChocolateText
import com.delacruz.catalogo.ui.theme.CoralAccent
import com.delacruz.catalogo.ui.theme.CreamSurface
import com.delacruz.catalogo.ui.theme.ItemImageBackground
import com.delacruz.catalogo.ui.theme.MutedBrown
import com.delacruz.catalogo.ui.theme.StockGreen
import java.util.Locale

/**
 * Tarjeta de un producto para la grilla del catálogo y de favoritos (H10).
 *
 * El corazón se marca o desmarca con [onAlternarFavorito]; la pantalla solo
 * avisa al ViewModel, no toca Room.
 */
@Composable
fun TarjetaProducto(
    item: Item,
    onClick: () -> Unit,
    onAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CreamSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .background(ItemImageBackground)
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(136.dp)
                    .padding(10.dp)
            )

            Surface(
                shape = CircleShape,
                color = CreamSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(34.dp)
            ) {
                IconButton(
                    onClick = onAlternarFavorito,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (item.esFavorito) R.drawable.ic_corazon else R.drawable.ic_corazon_borde
                        ),
                        contentDescription = if (item.esFavorito) {
                            "Quitar de favoritos"
                        } else {
                            "Agregar a favoritos"
                        },
                        tint = if (item.esFavorito) CoralAccent else MutedBrown,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                text = item.titulo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = ChocolateText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.marca,
                style = MaterialTheme.typography.labelMedium,
                color = CoralAccent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Chocolate)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = String.format(Locale.getDefault(), "S/ %.2f", item.precio),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = CreamSurface
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(R.drawable.ic_estrella),
                    contentDescription = null,
                    tint = CoralAccent,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = String.format(Locale.getDefault(), "%.1f", item.calificacion),
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedBrown
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (item.stock > 0) StockGreen else MutedBrown)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (item.stock > 0) "${item.stock} en stock" else "Agotado",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedBrown,
                    maxLines = 1
                )
            }
        }
    }
}
