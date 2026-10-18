package com.delacruz.huellitasvet.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Campo de texto de la app: la etiqueta va dentro del campo (arriba del valor), con icono
// opcional a la izquierda del color del perfil y el mensaje de error debajo del recuadro.
// Si esClave es true, oculta lo escrito con puntos (campo Contrasena del login)
@Composable
fun VetTextField(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    icono: ImageVector? = null,
    esClave: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    error: String? = null,
    lineasMinimas: Int = 1
) {
    val forma = RoundedCornerShape(14.dp)
    var enfocado by remember { mutableStateOf(false) }

    // El borde cambia segun el estado: error, enfocado o normal
    val colorBorde = when {
        error != null -> MaterialTheme.colorScheme.error
        enfocado -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Column(modifier = modifier) {
        TextField(
            value = valor,
            onValueChange = onCambio,
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = colorBorde, shape = forma)
                .onFocusChanged { enfocado = it.isFocused },
            label = { Text(text = etiqueta) },
            leadingIcon = if (icono != null) {
                { Icon(imageVector = icono, contentDescription = null) }
            } else null,
            visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esClave) KeyboardType.Password else tipoTeclado
            ),
            isError = error != null,
            singleLine = lineasMinimas == 1,
            minLines = lineasMinimas,
            shape = forma,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                // Sin linea inferior: el borde lo dibuja el modifier de arriba
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                errorLeadingIconColor = MaterialTheme.colorScheme.error
            )
        )
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}