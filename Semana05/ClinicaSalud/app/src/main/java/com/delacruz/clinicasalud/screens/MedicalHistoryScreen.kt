package com.delacruz.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Estructura de datos para representar un registro del historial medico.
 */
data class RegistroHistorial(
    val id: Int,
    val fecha: String,
    val diagnostico: String,
    val doctor: String,
    val tratamiento: String
)

/**
 * Composable que despliega la pantalla del Historial Medico del paciente.
 *
 * @param onVolverInicio Callback para regresar a la pantalla principal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalHistoryScreen(
    onVolverInicio: () -> Unit
) {
    // Datos de prueba para el historial de atenciones medicas
    val historialPrueba = listOf(
        RegistroHistorial(
            id = 1,
            fecha = "10 de Diciembre, 2025",
            diagnostico = "Chequeo General Preventivo",
            doctor = "Dra. Ana Torres",
            tratamiento = "Exámenes de rutina completados. Paciente en excelente estado de salud."
        ),
        RegistroHistorial(
            id = 2,
            fecha = "15 de Agosto, 2025",
            diagnostico = "Control Pedriátrico / Evaluación",
            doctor = "Dr. Luis Vega",
            tratamiento = "Actualización del esquema de vacunación y recomendaciones nutricionales."
        )
    )

    // Estructura principal con barra de navegacion superior
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial Médico", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // Boton de retorno a la pantalla anterior/inicio
                    IconButton(onClick = onVolverInicio) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Contenedor principal con listado vertical optimizado
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(historialPrueba) { registro ->
                // Renderizado de cada elemento del historial
                TarjetaHistorialItem(registro = registro)
            }
        }
    }
}

/**
 * Composable que dibuja una tarjeta detallada para cada registro del historial medico.
 *
 * @param registro Objeto con los detalles del diagnostico y tratamiento.
 */
@Composable
fun TarjetaHistorialItem(registro: RegistroHistorial) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado con icono de historial y fecha de atencion
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ListAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = registro.fecha,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Diagnostico principal de la consulta
            Text(
                text = registro.diagnostico,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Doctor tratante
            Text(
                text = "Atendido por: ${registro.doctor}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            // Detalle o nota del tratamiento medico
            Text(
                text = "Tratamiento / Notas:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
            Text(
                text = registro.tratamiento,
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}