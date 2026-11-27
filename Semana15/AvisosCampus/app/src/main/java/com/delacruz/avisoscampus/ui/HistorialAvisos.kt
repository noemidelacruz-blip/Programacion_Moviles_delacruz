package com.delacruz.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.avisoscampus.model.Aviso
import com.delacruz.avisoscampus.model.Origen
import com.delacruz.avisoscampus.ui.theme.Blanco
import com.delacruz.avisoscampus.ui.theme.EtiquetaBoton
import com.delacruz.avisoscampus.ui.theme.EtiquetaModoAvion
import com.delacruz.avisoscampus.ui.theme.EtiquetaRecordatorio
import com.delacruz.avisoscampus.ui.theme.FondoFila
import com.delacruz.avisoscampus.ui.theme.TextoEtiquetaOscuro
import com.delacruz.avisoscampus.ui.theme.TextoSecundario

// Solo recibe la lista: no conoce el ViewModel.
@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    TarjetaFuncion("Historial") {
        if (avisos.isEmpty()) {
            Text("Sin avisos todavía", color = TextoSecundario)
        } else {
            // Column y no LazyColumn: la pantalla ya se desplaza.
            avisos.take(20).forEach { aviso -> FilaAviso(aviso) }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = FondoFila,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EtiquetaOrigen(aviso.origen)
            Column(modifier = Modifier.weight(1f)) {
                Text(aviso.titulo, fontWeight = FontWeight.Bold)
                Text(
                    text = aviso.texto,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
            Text(
                text = aviso.hora,
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
        }
    }
}

@Composable
private fun EtiquetaOrigen(origen: Origen) {
    val (colorFondo, colorTexto) = when (origen) {
        Origen.BOTON -> EtiquetaBoton to Blanco
        Origen.RECORDATORIO -> EtiquetaRecordatorio to TextoEtiquetaOscuro
        Origen.MODO_AVION -> EtiquetaModoAvion to Blanco
    }
    Surface(
        color = colorFondo,
        contentColor = colorTexto,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = origen.texto,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
