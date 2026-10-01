package com.delacruz.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.delacruz.clinicasalud.navigation.Cita

/**
 * Composable que despliega el listado de citas medicas agendadas por el paciente.
 *
 * @param onVolverInicio Callback para regresar a la pantalla principal.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    onVolverInicio: () -> Unit
) {
    // Lista de citas de ejemplo para previsualizar el listado de reservas
    val listaCitas = listOf(
        Cita("Dra. Ana Torres", "Jue 26", "10:30", "Confirmada"),
        Cita("Dr. Luis Vega", "15 Ene", "11:00", "Completada")
    )

    // Estructura principal con barra superior
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // Boton de navegacion para regresar a la pantalla de inicio
                    IconButton(onClick = onVolverInicio) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver al inicio")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Listado vertical de citas optimizado con LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaCitas) { cita ->
                // Renderizado de cada tarjeta individual de cita
                TarjetaCitaItem(cita = cita)
            }
        }
    }
}

/**
 * Composable que dibuja la tarjeta contenedora con los detalles de una cita especifica.
 *
 * @param cita Objeto con la informacion de la cita a renderizar.
 */
@Composable
fun TarjetaCitaItem(cita: Cita) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono representativo de calendario/cita
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )

            // Columna informativa con medico y horario
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cita.doctorNombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${cita.fecha} - ${cita.hora}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Etiqueta distintiva (Badge) para mostrar el estado de la cita
            val esConfirmada = cita.estado == "Confirmada"
            val colorFondo = if (esConfirmada) Color(0xFFE8F5E9) else Color(0xFFE0E0E0)
            val colorTexto = if (esConfirmada) Color(0xFF2E7D32) else Color(0xFF616161)

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colorFondo
            ) {
                Text(
                    text = cita.estado,
                    color = colorTexto,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}