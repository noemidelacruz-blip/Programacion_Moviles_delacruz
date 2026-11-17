package com.delacruz.campusgps.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// El diseño del laboratorio usa siempre el tema claro, por eso no se
// usa el color dinámico ni un esquema oscuro.
private val EsquemaClaro = lightColorScheme(
    primary = AzulCampus,
    onPrimary = Color.White,
    primaryContainer = AzulSuave,
    onPrimaryContainer = AzulOscuro,
    secondary = AzulClaro,
    onSecondary = Color.White,
    background = FondoClaro,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = AzulSuave,
    onSurfaceVariant = TextoSecundario,
    error = RojoError,
    onError = Color.White,
    errorContainer = FondoError,
    onErrorContainer = RojoError
)

@Composable
fun CampusGPSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}
