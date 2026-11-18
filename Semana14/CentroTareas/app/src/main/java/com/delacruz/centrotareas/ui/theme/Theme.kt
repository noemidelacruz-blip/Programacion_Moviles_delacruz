package com.delacruz.centrotareas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = OutlinedBtnText,
    onPrimary = Color.White,
    primaryContainer = IconBoxBg,
    onPrimaryContainer = CardTitleColor,
    secondary = PrimaryBtnStart,
    onSecondary = Color.White,
    background = ScreenBgMiddle,
    onBackground = TextPrimary,
    surface = CardBackground,
    onSurface = TextPrimary
)

@Composable
fun CentroTareasTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
