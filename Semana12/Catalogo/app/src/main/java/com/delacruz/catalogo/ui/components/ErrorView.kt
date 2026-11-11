package com.delacruz.catalogo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.delacruz.catalogo.ui.theme.CatalogoTheme

/**
 * Vista de error reutilizable.
 *
 * Se muestra cuando el ViewModel está en estado UiState.Error.
 * Siempre incluye el botón Reintentar (requisito R03).
 */
@Composable
fun ErrorView(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No se pudo cargar la información",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = onReintentar,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(text = "Reintentar")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewPreview() {
    CatalogoTheme {
        ErrorView(
            mensaje = "No hay conexión a internet",
            onReintentar = {}
        )
    }
}
