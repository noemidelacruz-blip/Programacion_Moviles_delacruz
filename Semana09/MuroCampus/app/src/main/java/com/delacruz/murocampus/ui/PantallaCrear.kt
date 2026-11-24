package com.delacruz.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.murocampus.model.Post
import com.delacruz.murocampus.ui.theme.FondoError
import com.delacruz.murocampus.ui.theme.FondoExito
import com.delacruz.murocampus.ui.theme.FormaCampo
import com.delacruz.murocampus.ui.theme.FormaPildora
import com.delacruz.murocampus.ui.theme.FormaTarjeta
import com.delacruz.murocampus.ui.theme.MoradoClaro
import com.delacruz.murocampus.ui.theme.MoradoPrincipal
import com.delacruz.murocampus.ui.theme.SuperficieBlanca
import com.delacruz.murocampus.ui.theme.TextoCuerpo
import com.delacruz.murocampus.ui.theme.TextoError
import com.delacruz.murocampus.ui.theme.TextoExito
import com.delacruz.murocampus.viewmodel.UiState

/**
 * Pantalla para crear una publicación con POST. El botón solo se habilita con
 * los dos campos escritos; al confirmar se muestra el id que devuelve el servidor.
 */
@Composable
fun PantallaCrear(
    estado: UiState<Post>?,
    onPublicar: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }

    // Cuando el servidor confirma, se limpia el formulario.
    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        CabeceraMuro(
            mostrarAtras = true,
            onAtras = onVolver
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                singleLine = true,
                shape = FormaCampo,
                colors = coloresCampo(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contenido,
                onValueChange = { contenido = it },
                label = { Text("Contenido") },
                minLines = 4,
                shape = FormaCampo,
                colors = coloresCampo(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { onPublicar(titulo.trim(), contenido.trim()) },
                enabled = titulo.isNotBlank() &&
                    contenido.isNotBlank() &&
                    estado !is UiState.Cargando,
                shape = FormaPildora,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MoradoPrincipal,
                    contentColor = Color.White,
                    disabledContainerColor = MoradoClaro,
                    disabledContentColor = MoradoPrincipal
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(220.dp)
                    .height(54.dp)
            ) {
                if (estado is UiState.Cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                } else {
                    IconoEnviar(color = Color.White, tamano = 18.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = "Publicar",
                    fontWeight = FontWeight.Bold
                )
            }

            when (estado) {
                is UiState.Exito -> Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = FormaTarjeta,
                    color = FondoExito
                ) {
                    Text(
                        text = "Publicado con id ${estado.datos.id}",
                        color = TextoExito,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is UiState.Error -> Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = FormaTarjeta,
                    color = FondoError
                ) {
                    Text(
                        text = estado.mensaje,
                        color = TextoError,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is UiState.Cargando, null -> Unit
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

/** Campos blancos con esquinas de 24 dp, borde morado al enfocar. */
@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextoCuerpo,
    unfocusedTextColor = TextoCuerpo,
    focusedContainerColor = SuperficieBlanca,
    unfocusedContainerColor = SuperficieBlanca,
    cursorColor = MoradoPrincipal,
    focusedBorderColor = MoradoPrincipal,
    unfocusedBorderColor = MoradoClaro,
    focusedLabelColor = MoradoPrincipal,
    unfocusedLabelColor = TextoCuerpo
)
