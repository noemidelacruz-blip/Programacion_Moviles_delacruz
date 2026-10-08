package com.delacruz.saludpluscitas.ui.screens.resultados

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.model.Resultado
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BarraNavegacion
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.components.formatearFecha
import com.delacruz.saludpluscitas.ui.theme.AzulClaro
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.BordeSuave
import com.delacruz.saludpluscitas.ui.theme.NaranjaPastel
import com.delacruz.saludpluscitas.ui.theme.NaranjaTexto
import com.delacruz.saludpluscitas.ui.theme.SuperficieBlanca
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario
import com.delacruz.saludpluscitas.ui.theme.VerdePastel
import com.delacruz.saludpluscitas.ui.theme.VerdeTexto

// Lista fija de resultados de ejemplo (no hay base de datos).
private val resultados = listOf(
    Resultado(
        1, "Presión arterial", "Cardiología", "2026-10-05",
        "118/76 mmHg", "90/60 - 120/80 mmHg", "Normal"
    ),
    Resultado(
        2, "Colesterol total", "Cardiología", "2026-09-25",
        "185 mg/dL", "Menor a 200 mg/dL", "Normal"
    ),
    Resultado(
        3, "Hemoglobina", "Medicina General", "2026-09-18",
        "14.2 g/dL", "12.0 - 16.0 g/dL", "Normal"
    ),
    Resultado(
        4, "Glucosa en ayunas", "Medicina General", "2026-09-18",
        "104 mg/dL", "70 - 99 mg/dL", "Revisar con tu médico"
    ),
    Resultado(
        5, "Agudeza visual", "Oftalmología", "2026-10-02",
        "20/20", "20/20", "Normal"
    )
)

@Composable
fun ResultadosScreen(
    navController: NavController
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Resultados",
                onAtras = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BarraNavegacion(
                navController = navController,
                rutaActual = Rutas.Resultados.ruta
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(resultados, key = { it.id }) { resultado ->
                TarjetaResultado(resultado = resultado)
            }
        }
    }
}

// Tarjeta de un resultado: examen, especialidad, fecha, valor medido,
// rango de referencia y un chip de estado (verde si es normal, naranja si no).
@Composable
private fun TarjetaResultado(
    resultado: Resultado
) {
    val esNormal = resultado.estado == "Normal"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SuperficieBlanca),
        border = BorderStroke(1.dp, BordeSuave),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = AzulPrimario
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = resultado.titulo,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(text = resultado.especialidad, fontSize = 13.sp, color = TextoSecundario)
                    Text(
                        text = formatearFecha(resultado.fecha),
                        fontSize = 13.sp,
                        color = TextoSecundario
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = resultado.valor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario
                    )
                    Text(
                        text = "Referencia: ${resultado.referencia}",
                        fontSize = 13.sp,
                        color = TextoSecundario
                    )
                }
                Text(
                    text = resultado.estado,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (esNormal) VerdeTexto else NaranjaTexto,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (esNormal) VerdePastel else NaranjaPastel)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}