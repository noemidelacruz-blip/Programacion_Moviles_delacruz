package com.delacruz.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Paleta única de MuroCampus. Todos los roles de color salen de los mismos
 * valores para que ningún componente inventario tonos nuevos.
 */
private val EsquemaMuroCampus = lightColorScheme(
    primary = MoradoPrincipal,
    onPrimary = Color.White,
    primaryContainer = MoradoClaro,
    onPrimaryContainer = MoradoPrincipal,
    secondary = MoradoPrincipal,
    onSecondary = Color.White,
    secondaryContainer = MoradoClaro,
    onSecondaryContainer = MoradoPrincipal,
    tertiary = Durazno,
    onTertiary = TextoDurazno,
    background = FondoCrema,
    onBackground = TextoCuerpo,
    surface = SuperficieBlanca,
    onSurface = TextoCuerpo,
    surfaceVariant = MoradoClaro,
    onSurfaceVariant = TextoCuerpo,
    error = TextoError,
    onError = Color.White,
    errorContainer = FondoError,
    onErrorContainer = TextoError,
    outline = MoradoClaro,
    outlineVariant = MoradoClaro
)

/**
 * Tema de la aplicación. La paleta es fija: el color dinámico queda apagado
 * (parámetro `dynamicColor = false`) y no hay un esquema oscuro alternativo,
 * así la app siempre se ve como el diseño aunque el teléfono cambie de tema.
 */
@Composable
fun MuroCampusTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // `darkTheme` y `dynamicColor` se conservan por compatibilidad y se ignoran
    // a propósito: la identidad visual de MuroCampus no cambia con el sistema.
    MaterialTheme(
        colorScheme = EsquemaMuroCampus,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
