package com.delacruz.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.murocampus.model.Comentario
import com.delacruz.murocampus.ui.theme.FormaTarjeta
import com.delacruz.murocampus.ui.theme.MoradoClaro
import com.delacruz.murocampus.ui.theme.MoradoPrincipal
import com.delacruz.murocampus.ui.theme.SuperficieBlanca
import com.delacruz.murocampus.ui.theme.TextoCuerpo
import com.delacruz.murocampus.viewmodel.DetalleUi
import com.delacruz.murocampus.viewmodel.UiState

/**
 * Pantalla de detalle: publicación completa y debajo la lista de comentarios.
 * Solo dibuja el estado que recibe del ViewModel. La flecha de la cabecera
 * vuelve a la lista sin tocar la lógica de navegación.
 */
@Composable
fun PantallaDetalle(
    estado: UiState<DetalleUi>,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxSize()) {
        CabeceraMuro(
            mostrarAtras = true,
            onAtras = onVolver
        )

        Box(modifier = Modifier.weight(1f)) {
            when (estado) {
                is UiState.Cargando -> Cargando()

                is UiState.Error -> ErrorConReintento(
                    mensaje = estado.mensaje,
                    onReintentar = onReintentar
                )

                is UiState.Exito -> DetalleContenido(detalle = estado.datos)
            }
        }
    }
}

@Composable
private fun DetalleContenido(
    detalle: DetalleUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PublicacionCompleta(detalle)

        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoBurbuja(color = MoradoPrincipal, tamano = 20.dp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Comentarios (${detalle.comentarios.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MoradoPrincipal
            )
        }

        detalle.comentarios.forEach { comentario ->
            TarjetaComentario(comentario)
        }
    }
}

@Composable
private fun PublicacionCompleta(detalle: DetalleUi) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = FormaTarjeta,
        color = SuperficieBlanca,
        border = BorderStroke(width = 1.dp, color = MoradoClaro)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = detalle.post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MoradoPrincipal
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = detalle.post.body,
                style = MaterialTheme.typography.bodyMedium,
                color = TextoCuerpo
            )
        }
    }
}

@Composable
private fun TarjetaComentario(comentario: Comentario) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = FormaTarjeta,
        color = MoradoClaro
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = comentario.email,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MoradoPrincipal
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = comentario.body,
                style = MaterialTheme.typography.bodyMedium,
                color = TextoCuerpo
            )
        }
    }
}
