
package com.delacruz.catalogo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PeachPrimary,
    onPrimary = BrownText,

    secondary = CoralAccent,
    onSecondary = SoftWhite,

    tertiary = MutedBrown,
    onTertiary = SoftWhite,

    background = CreamBackground,
    onBackground = BrownText,

    surface = SoftWhite,
    onSurface = BrownText,

    surfaceVariant = SurfaceVariantBg,
    onSurfaceVariant = MutedBrown,

    surfaceContainer = PeachPrimary
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
