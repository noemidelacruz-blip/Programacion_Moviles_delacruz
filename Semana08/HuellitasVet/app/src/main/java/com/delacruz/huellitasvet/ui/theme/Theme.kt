package com.delacruz.huellitasvet.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.model.Rol

// ---------- Dueno: esquema claro (esmeralda) ----------
private val EsquemaDuenoClaro = lightColorScheme(
    primary = EsmeraldaPrimario,
    onPrimary = Color.White,
    primaryContainer = EsmeraldaContenedor,
    onPrimaryContainer = EsmeraldaTextoContenedor,
    secondary = EsmeraldaPrimario,
    onSecondary = Color.White,
    secondaryContainer = EsmeraldaContenedor,
    onSecondaryContainer = EsmeraldaTextoContenedor,
    background = EsmeraldaFondo,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = EsmeraldaContenedor,
    onSurfaceVariant = TextoSecundario,
    outline = Borde,
    outlineVariant = Borde,
    error = RojoAccion,
    onError = Color.White,
    errorContainer = RojoSuave,
    onErrorContainer = RojoAccion
)

// ---------- Dueno: esquema oscuro ----------
private val EsquemaDuenoOscuro = darkColorScheme(
    primary = EsmeraldaClaro,
    onPrimary = Color(0xFF00281C),
    primaryContainer = EsmeraldaOscuroContenedor,
    onPrimaryContainer = EsmeraldaContenedor,
    secondary = EsmeraldaClaro,
    onSecondary = Color(0xFF00281C),
    secondaryContainer = EsmeraldaOscuroContenedor,
    onSecondaryContainer = EsmeraldaContenedor,
    background = EsmeraldaOscuroFondo,
    onBackground = TextoClaroSobreOscuro,
    surface = EsmeraldaOscuroSuperficie,
    onSurface = TextoClaroSobreOscuro,
    surfaceVariant = EsmeraldaOscuroContenedor,
    onSurfaceVariant = TextoSecundarioOscuro,
    outline = Color(0xFF2F4A3F),
    outlineVariant = Color(0xFF2F4A3F),
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2)
)

// ---------- Veterinario: esquema claro (azul) ----------
private val EsquemaVetClaro = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulTextoContenedor,
    secondary = AzulPrimario,
    onSecondary = Color.White,
    secondaryContainer = AzulContenedor,
    onSecondaryContainer = AzulTextoContenedor,
    background = AzulFondo,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = AzulContenedor,
    onSurfaceVariant = TextoSecundario,
    outline = Color(0xFFD5DEF0),
    outlineVariant = Color(0xFFD5DEF0),
    error = RojoAccion,
    onError = Color.White,
    errorContainer = RojoSuave,
    onErrorContainer = RojoAccion
)

// ---------- Veterinario: esquema oscuro ----------
private val EsquemaVetOscuro = darkColorScheme(
    primary = AzulClaro,
    onPrimary = Color(0xFF071A3D),
    primaryContainer = AzulOscuroContenedor,
    onPrimaryContainer = AzulContenedor,
    secondary = AzulClaro,
    onSecondary = Color(0xFF071A3D),
    secondaryContainer = AzulOscuroContenedor,
    onSecondaryContainer = AzulContenedor,
    background = AzulOscuroFondo,
    onBackground = TextoClaroSobreOscuro,
    surface = AzulOscuroSuperficie,
    onSurface = TextoClaroSobreOscuro,
    surfaceVariant = AzulOscuroContenedor,
    onSurfaceVariant = TextoSecundarioOscuro,
    outline = Color(0xFF2B3A5E),
    outlineVariant = Color(0xFF2B3A5E),
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2)
)

// Esquinas redondeadas como en el mockup (tarjetas, botones y chips)
private val VetShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

// Tema de la app: elige la paleta segun el perfil (Dueno o Veterinario) y el modo oscuro.
// En las pantallas se usa siempre MaterialTheme.colorScheme.primary, nunca un color fijo
@Composable
fun VetTheme(
    rol: Rol = Rol.CLIENTE,
    modoOscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val esquema = when (rol) {
        Rol.CLIENTE -> if (modoOscuro) EsquemaDuenoOscuro else EsquemaDuenoClaro
        Rol.ADMIN -> if (modoOscuro) EsquemaVetOscuro else EsquemaVetClaro
    }

    MaterialTheme(
        colorScheme = esquema,
        typography = VetTypography,
        shapes = VetShapes,
        content = content
    )
}