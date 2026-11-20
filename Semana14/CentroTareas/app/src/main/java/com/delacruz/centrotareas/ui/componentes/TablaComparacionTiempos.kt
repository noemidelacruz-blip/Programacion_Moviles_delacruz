package com.delacruz.centrotareas.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.centrotareas.ui.theme.CardTitleColor
import com.delacruz.centrotareas.ui.theme.OutlinedBtnBorder
import com.delacruz.centrotareas.ui.theme.ScreenBgMiddle
import com.delacruz.centrotareas.ui.theme.SuccessGreenText
import com.delacruz.centrotareas.ui.theme.TextPrimary
import com.delacruz.centrotareas.ui.theme.TextSecondary
import com.delacruz.centrotareas.viewmodel.TiempoCarga

@Composable
internal fun TablaComparacionTiempos(historial: List<TiempoCarga>) {
    val unaPorUna = historial.filter { it.modo == "Una por una" }
    val aLaVez = historial.filter { it.modo == "A la vez" }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ScreenBgMiddle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.BarChart,
                    contentDescription = null,
                    tint = CardTitleColor,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Comparación de tiempos",
                    color = CardTitleColor,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Últimas ${historial.size} corridas",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            FilaTiempos(listOf("Modo", "Veces", "Total", "Promedio"), encabezado = true)
            HorizontalDivider(color = OutlinedBtnBorder)
            FilaTiempos(
                listOf(
                    "Una por una",
                    unaPorUna.size.toString(),
                    total(unaPorUna),
                    promedio(unaPorUna)
                )
            )
            FilaTiempos(
                listOf(
                    "A la vez",
                    aLaVez.size.toString(),
                    total(aLaVez),
                    promedio(aLaVez)
                )
            )

            HorizontalDivider(color = OutlinedBtnBorder)
            Text(
                text = mensajeDiferencia(unaPorUna, aLaVez),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = SuccessGreenText
            )
        }
    }
}

@Composable
internal fun FilaTiempos(valores: List<String>, encabezado: Boolean = false) {
    val pesos = listOf(1.15f, 0.6f, 0.95f, 1.05f)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (indice in valores.indices) {
            val valor = valores[indice]
            Text(
                text = valor,
                modifier = Modifier.weight(pesos[indice]),
                color = if (encabezado) CardTitleColor else TextPrimary,
                style = if (encabezado) {
                    MaterialTheme.typography.labelMedium
                } else {
                    MaterialTheme.typography.bodySmall
                },
                fontWeight = if (encabezado) FontWeight.Bold else FontWeight.Normal,
                textAlign = if (indice == 0) TextAlign.Start else TextAlign.End,
                maxLines = 1
            )
        }
    }
}

internal fun total(lista: List<TiempoCarga>): String {
    if (lista.isEmpty()) return "—"
    return "${lista.sumOf { it.milisegundos }} ms"
}

internal fun promedio(lista: List<TiempoCarga>): String {
    if (lista.isEmpty()) return "—"
    return "${lista.sumOf { it.milisegundos } / lista.size} ms"
}

internal fun mensajeDiferencia(
    unaPorUna: List<TiempoCarga>,
    aLaVez: List<TiempoCarga>
): String {
    if (unaPorUna.isEmpty() || aLaVez.isEmpty()) {
        return "Ejecuta Una por una y A la vez para ver la diferencia."
    }

    val promedioUna = unaPorUna.sumOf { it.milisegundos } / unaPorUna.size
    val promedioVez = aLaVez.sumOf { it.milisegundos } / aLaVez.size
    val diferencia = promedioUna - promedioVez

    return when {
        diferencia > 0 -> {
            val porcentaje = if (promedioUna > 0) diferencia * 100 / promedioUna else 0
            "A la vez es $diferencia ms más rápido ($porcentaje % menos)."
        }
        diferencia < 0 -> "Una por una es ${-diferencia} ms más rápido."
        else -> "Ambos modos tardan lo mismo en promedio."
    }
}
