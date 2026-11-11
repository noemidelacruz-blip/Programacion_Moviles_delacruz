package com.delacruz.catalogo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.catalogo.R
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.CoralAccent
import com.delacruz.catalogo.ui.theme.CreamSurface

/**
 * Barra superior reutilizable.
 *
 * Muestra la marca BeautyEmi a la izquierda y el botón "Cerrar sesión" a la
 * derecha. Catálogo, Favoritos y el Panel Admin la usan para que ambos roles
 * tengan el cierre de sesión siempre en el mismo lugar.
 */
@Composable
fun BarraSuperior(
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
    onAbrirFavoritos: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "BeautyEmi",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = CoralAccent
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onAbrirFavoritos != null) {
                Surface(
                    shape = CircleShape,
                    color = CreamSurface,
                    shadowElevation = 2.dp
                ) {
                    IconButton(
                        onClick = onAbrirFavoritos,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bolsa),
                            contentDescription = "Ir a favoritos",
                            tint = Chocolate,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Surface(
                shape = RoundedCornerShape(50),
                color = CreamSurface,
                shadowElevation = 2.dp
            ) {
                TextButton(
                    onClick = onCerrarSesion,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_salir),
                        contentDescription = null,
                        tint = Chocolate,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Cerrar sesión",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Chocolate
                    )
                }
            }
        }
    }
}
