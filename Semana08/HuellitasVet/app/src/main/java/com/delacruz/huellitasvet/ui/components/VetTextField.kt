package com.delacruz.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// Campo de texto de la app: etiqueta, icono opcional a la izquierda y mensaje de error debajo.
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
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        modifier = modifier,
        label = { Text(text = etiqueta) },
        leadingIcon = if (icono != null) {
            { Icon(imageVector = icono, contentDescription = null) }
        } else null,
        visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esClave) KeyboardType.Password else tipoTeclado
        ),
        isError = error != null,
        supportingText = if (error != null) {
            { Text(text = error) }
        } else null,
        singleLine = lineasMinimas == 1,
        minLines = lineasMinimas,
        shape = RoundedCornerShape(14.dp)
    )
}