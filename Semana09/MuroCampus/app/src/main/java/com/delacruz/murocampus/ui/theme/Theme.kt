package com.delacruz.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = AzulMuro,
    onPrimary = Color.White,
    primaryContainer = AzulMuroClaro,
    onPrimaryContainer = AzulMuroOscuro,
    secondary = AzulMuroOscuro,
    onSecondary = Color.White,
    secondaryContainer = AzulMuroClaro,
    onSecondaryContainer = AzulMuroOscuro,
    tertiary = NaranjaUsuario,
    onTertiary = NaranjaUsuarioOscuro,
    background = FondoMuro,
    onBackground = TextoPrincipal,
    surface = SuperficieMuro,
    onSurface = TextoPrincipal,
    surfaceVariant = Color(0xFFEDF1F5),
    onSurfaceVariant = TextoSecundario,
    error = TextoError,
    onError = Color.White,
    errorContainer = FondoError,
    onErrorContainer = TextoError,
    outline = Color(0xFFB6C2CC)
)

private val EsquemaOscuro = darkColorScheme(
    primary = AzulMuroClaro,
    onPrimary = AzulMuroOscuro,
    primaryContainer = AzulMuroOscuro,
    onPrimaryContainer = AzulMuroClaro,
    secondary = AzulMuroClaro,
    onSecondary = AzulMuroOscuro,
    tertiary = NaranjaUsuario,
    onTertiary = NaranjaUsuarioOscuro,
    background = Color(0xFF101619),
    onBackground = Color(0xFFE6EDF2),
    surface = Color(0xFF16202A),
    onSurface = Color(0xFFE6EDF2),
    surfaceVariant = Color(0xFF22303B),
    onSurfaceVariant = Color(0xFFC3D0DA),
    errorContainer = TextoError,
    onErrorContainer = FondoError
)

/**
 * Tema de la aplicación. No usamos color dinámico ni modo oscuro automático para
 * conservar la identidad azul de MuroCampus que muestra el laboratorio.
 */
@Composable
fun MuroCampusTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        content = content
    )
}
