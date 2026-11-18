package com.delacruz.centrotareas.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AzulClaro,
    onPrimary = AzulOscuro,
    secondary = AzulClaro,
    onSecondary = AzulOscuro,
    background = Color(0xFF101418),
    onBackground = Color(0xFFE3E6E8),
    surface = Color(0xFF1A1F24),
    onSurface = Color(0xFFE3E6E8)
)

private val LightColorScheme = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = AzulPrincipal,
    onSecondary = Color.White,
    background = FondoClaro,
    onBackground = TextoOscuro,
    surface = SuperficieClara,
    onSurface = TextoOscuro
)

@Composable
fun CentroTareasTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
