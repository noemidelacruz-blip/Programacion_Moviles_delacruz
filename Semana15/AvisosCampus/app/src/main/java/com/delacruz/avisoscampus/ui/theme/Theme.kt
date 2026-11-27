package com.delacruz.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaCampus = lightColorScheme(
    primary = AzulCampus,
    onPrimary = Blanco,
    background = FondoClaro,
    surface = Blanco
)

@Composable
fun AvisosCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaCampus,
        typography = Typography,
        content = content
    )
}
