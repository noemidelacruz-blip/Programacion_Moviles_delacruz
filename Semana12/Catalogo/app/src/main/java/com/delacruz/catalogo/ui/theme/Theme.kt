package com.delacruz.catalogo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Tema claro de BeautyEmi.
 *
 * Solo hay un tema (claro) para que la app se vea igual en todos los equipos.
 * Los nombres vienen de la paleta de las capturas: chocolate para acciones,
 * durazno para el fondo y crema para las tarjetas.
 */
private val LightColorScheme = lightColorScheme(
    // Acciones principales: botones, barras superiores, pestaña activa.
    primary = Chocolate,
    onPrimary = CreamSurface,
    primaryContainer = Chocolate,
    onPrimaryContainer = CreamSurface,
    inversePrimary = PeachSoft,

    // Acentos: categorías, títulos de producto y precios.
    secondary = CoralAccent,
    onSecondary = CreamSurface,
    secondaryContainer = PeachSoft,
    onSecondaryContainer = ChocolateText,

    // Textos secundarios y detalles.
    tertiary = MutedBrown,
    onTertiary = CreamSurface,
    tertiaryContainer = PeachSoft,
    onTertiaryContainer = ChocolateText,

    // Fondo general durazno y textos chocolate.
    background = PeachBackground,
    onBackground = ChocolateText,

    // Tarjetas y superficies crema.
    surface = CreamSurface,
    onSurface = ChocolateText,
    surfaceVariant = PeachSoft,
    onSurfaceVariant = MutedBrown,
    surfaceTint = Chocolate,
    surfaceBright = CreamSurface,
    surfaceDim = PeachSoft,
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = CreamSurface,
    surfaceContainer = Color(0xFFFDF0E7),
    surfaceContainerHigh = Color(0xFFFBE9DE),
    surfaceContainerHighest = Color(0xFFF8E1D4),

    inverseSurface = ChocolateText,
    inverseOnSurface = CreamSurface,

    // Errores en terracota.
    error = TerracottaError,
    onError = CreamSurface,
    errorContainer = Color(0xFFF7D8D0),
    onErrorContainer = Color(0xFF5A2018),

    // Bordes de campos y botones con contorno.
    outline = OutlineSoft,
    outlineVariant = Color(0xFFEBC8B9),
    scrim = Color(0xFF000000)
)

@Composable
fun CatalogoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
