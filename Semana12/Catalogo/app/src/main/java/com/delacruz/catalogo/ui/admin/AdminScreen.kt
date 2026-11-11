package com.delacruz.catalogo.ui.admin

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.delacruz.catalogo.R
import com.delacruz.catalogo.ui.components.BarraSuperior
import com.delacruz.catalogo.ui.components.Cargando
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.ChocolateText
import com.delacruz.catalogo.ui.theme.CoralAccent
import com.delacruz.catalogo.ui.theme.CreamSurface
import com.delacruz.catalogo.ui.theme.MutedBrown
import com.delacruz.catalogo.ui.theme.PeachSoft
import com.delacruz.catalogo.ui.theme.TerracottaError

/**
 * Panel de administración (hito H14).
 *
 * Solo se muestra a la persona con rol admin (R02). Permite ver cuántos
 * productos hay guardados, cuántos son favoritos, sincronizar con la API y
 * limpiar la caché local (R10).
 *
 * La pantalla solo observa el ViewModel y llama a sus funciones.
 */
@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        BarraSuperior(onCerrarSesion = onCerrarSesion)

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Panel Admin",
            style = MaterialTheme.typography.headlineMedium,
            color = ChocolateText
        )

        Spacer(modifier = Modifier.height(18.dp))
        if (estado.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Cargando(mensaje = "Leyendo la caché local...")
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaTotal(
                    titulo = "Productos",
                    descripcion = "guardados",
                    valor = estado.totalGuardados,
                    icono = R.drawable.ic_bolsa,
                    modifier = Modifier.weight(1f)
                )
                TarjetaTotal(
                    titulo = "Favoritos",
                    descripcion = "marcados",
                    valor = estado.totalFavoritos,
                    icono = R.drawable.ic_corazon,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))


            Button(
                onClick = viewModel::sincronizar,
                enabled = !estado.trabajando,
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Chocolate,
                    contentColor = CreamSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_catalogo),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sincronizar",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = viewModel::limpiarCache,
                enabled = !estado.trabajando,
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TerracottaError
                ),
                border = BorderStroke(1.dp, TerracottaError.copy(alpha = 0.55f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Limpiar caché",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (estado.trabajando) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Chocolate,
                    trackColor = PeachSoft
                )
            } else {
                estado.mensaje?.let { texto ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CreamSurface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = texto,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ChocolateText,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

/** Tarjeta con un total grande y su descripción. */
@Composable
private fun TarjetaTotal(
    titulo: String,
    descripcion: String,
    valor: Int,
    icono: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CreamSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(PeachSoft, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icono),
                    contentDescription = null,
                    tint = CoralAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.headlineLarge,
                color = CoralAccent
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleSmall,
                color = ChocolateText
            )
            Text(
                text = descripcion,
                style = MaterialTheme.typography.labelSmall,
                color = MutedBrown
            )
        }
    }
}
