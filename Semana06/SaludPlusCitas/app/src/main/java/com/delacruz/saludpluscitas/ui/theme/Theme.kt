package com.delacruz.saludpluscitas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// El diseño de referencia es claro: se usa siempre el esquema claro.
private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = VerdeTexto,
    onSecondary = Color.White,
    background = FondoClaro,
    onBackground = TextoPrincipal,
    surface = SuperficieBlanca,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    outline = BordeSuave,
    error = RojoTexto
)

@Composable
fun SaludPlusCitasTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}