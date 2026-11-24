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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.murocampus.model.Comentario
import com.delacruz.murocampus.ui.theme.FondoError
import com.delacruz.murocampus.ui.theme.FormaPildora
import com.delacruz.murocampus.ui.theme.FormaTarjeta
import com.delacruz.murocampus.ui.theme.MoradoClaro
import com.delacruz.murocampus.ui.theme.MoradoPrincipal
import com.delacruz.murocampus.ui.theme.SuperficieBlanca
import com.delacruz.murocampus.ui.theme.TextoCuerpo
import com.delacruz.murocampus.ui.theme.TextoError
import com.delacruz.murocampus.viewmodel.DetalleUi
import com.delacruz.murocampus.viewmodel.UiState

/**
 * Pantalla de detalle: publicación completa, botón Eliminar y la lista de
 * comentarios. Solo dibuja lo que recibe: nunca llama a la API.
 */
@Composable
fun PantallaDetalle(
    estado: UiState<DetalleUi>,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    eliminacion: UiState<Unit>? = null,
    onEliminar: (Int) -> Unit = {}
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

                is UiState.Exito -> DetalleContenido(
                    detalle = estado.datos,
                    eliminacion = eliminacion,
                    onEliminar = onEliminar
                )
            }
        }
    }
}

@Composable
private fun DetalleContenido(
    detalle: DetalleUi,
    eliminacion: UiState<Unit>?,
    onEliminar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmando by rememberSaveable { mutableStateOf(false) }
    val eliminando = eliminacion is UiState.Cargando

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PublicacionCompleta(detalle)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = { confirmando = true },
                enabled = !eliminando,
                shape = FormaPildora,
                border = BorderStroke(width = 1.dp, color = TextoError),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextoError)
            ) {
                if (eliminando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = TextoError
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Eliminando…", fontWeight = FontWeight.Bold)
                } else {
                    IconoEliminar(color = TextoError, tamano = 16.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Eliminar", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (eliminacion is UiState.Error) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = FormaTarjeta,
                color = FondoError
            ) {
                Text(
                    text = eliminacion.mensaje,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoError,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }

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

    if (confirmando) {
        AlertDialog(
            onDismissRequest = { confirmando = false },
            title = { Text(text = "¿Eliminar publicación?") },
            text = {
                Text(
                    text = "Se enviará DELETE posts/${detalle.post.id} y la publicación " +
                        "saldrá del muro."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmando = false
                        onEliminar(detalle.post.id)
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = TextoError,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmando = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
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
