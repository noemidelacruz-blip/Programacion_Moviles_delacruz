package com.delacruz.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tema claro: es el diseño vino que ya tenía la app
private val EsquemaClaro = lightColorScheme(
    primary = VinoCampus,
    onPrimary = Color.White,
    primaryContainer = RosaSuave,
    onPrimaryContainer = VinoOscuro,
    secondary = RosaCampus,
    onSecondary = Color.White,
    background = FondoClaro,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = RosaSuave,
    onSurfaceVariant = TextoSecundario,
    error = RojoError,
    onError = Color.White,
    errorContainer = FondoError,
    onErrorContainer = RojoError
)

// Tema oscuro: mismos acentos vino, pero aclarados para que se lean sobre negro.
// El mapa también cambia a su estilo nocturno cuando este esquema está activo.
private val EsquemaOscuro = darkColorScheme(
    primary = VinoClaro,
    onPrimary = VinoOscuro,
    primaryContainer = VinoContenedorOscuro,
    onPrimaryContainer = RosaSuave,
    secondary = RosaCampus,
    onSecondary = Color.White,
    background = FondoOscuro,
    onBackground = TextoClaro,
    surface = SuperficieOscura,
    onSurface = TextoClaro,
    surfaceVariant = SuperficieOscuraAlta,
    onSurfaceVariant = TextoSecundarioOscuro,
    outline = TextoSecundarioOscuro,
    outlineVariant = BordeOscuro,
    error = RojoErrorClaro,
    onError = VinoOscuro,
    errorContainer = FondoErrorOscuro,
    onErrorContainer = RojoErrorClaro
)

// La app sigue la configuración del sistema: claro u oscuro
@Composable
fun CampusGPSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
