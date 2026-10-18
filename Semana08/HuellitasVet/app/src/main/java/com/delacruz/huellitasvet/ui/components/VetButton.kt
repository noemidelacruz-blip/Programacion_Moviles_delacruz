package com.delacruz.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// Tipos de boton: LLENO (accion principal) o CONTORNO (accion secundaria, como Cancelar)
enum class VetBotonTipo { LLENO, CONTORNO }

// Boton de la app. Por defecto usa el color primario del perfil y ocupa todo el ancho.
// Para botones en rojo (Cancelar cita, Cerrar sesion) se pasa color = MaterialTheme.colorScheme.error
@Composable
fun VetButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    tipo: VetBotonTipo = VetBotonTipo.LLENO,
    color: Color = MaterialTheme.colorScheme.primary,
    icono: ImageVector? = null,
    habilitado: Boolean = true
) {
    val forma = RoundedCornerShape(14.dp)
    val alto = Modifier.height(50.dp)

    // Contenido comun: icono opcional + texto
    val contenido: @Composable RowScope.() -> Unit = {
        if (icono != null) {
            Icon(imageVector = icono, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = texto, style = MaterialTheme.typography.labelLarge)
    }

    if (tipo == VetBotonTipo.LLENO) {
        Button(
            onClick = onClick,
            modifier = modifier.then(alto),
            enabled = habilitado,
            shape = forma,
            colors = ButtonDefaults.buttonColors(containerColor = color),
            content = contenido
        )
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.then(alto),
            enabled = habilitado,
            shape = forma,
            border = BorderStroke(1.5.dp, color),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
            content = contenido
        )
    }
}