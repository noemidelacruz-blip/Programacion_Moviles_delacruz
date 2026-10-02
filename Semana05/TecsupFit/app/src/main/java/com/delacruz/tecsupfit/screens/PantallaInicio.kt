package com.delacruz.tecsupfit.screens

// Importaciones necesarias de Jetpack Compose y Material 3
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.delacruz.tecsupfit.model.ClaseFit

// Paleta de colores institucional para la aplicación
val VerdeTecsup = Color(0xFF006847)
val VerdeClaroBg = Color(0xFFE2F3EC)
val GrisTarjeta = Color(0xFFF0F0F0)

/**
 * Pantalla principal que muestra el banner de bienvenida, filtros de fecha y la lista de clases.
 * @param onClaseClick Callback que se ejecuta cuando el usuario selecciona una clase para ver su detalle.
 */
@Composable
fun PantallaInicio(onClaseClick: (ClaseFit) -> Unit) {
    // Estado local para controlar el filtro seleccionado ("Hoy" o "Esta semana")
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    // Lista de clases simuladas para la vista de inicio
    val clases = listOf(
        ClaseFit(1, "Yoga funcional", "7:00 am", "Sala 2"),
        ClaseFit(2, "Cross Training", "6:00 pm", "Sala 1"),
        ClaseFit(3, "Spinning", "7:30 pm", "Sala 3")
    )

    // Layout principal dispuesto de forma vertical
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Banner superior verde con estilo institucional
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeTecsup)
                .padding(20.dp)
        ) {
            Column {
                Text("TECSUP Fit", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Hola, Noemi", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista horizontal (LazyRow) para los chips de filtrado
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("Hoy", "Esta semana")) { filtro ->
                FilterChip(
                    selected = filtroSeleccionado == filtro,
                    onClick = { filtroSeleccionado = filtro },
                    label = { Text(filtro) },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeTecsup,
                        selectedLabelColor = Color.White,
                        containerColor = GrisTarjeta,
                        labelColor = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Clases disponibles", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(12.dp))

        // Lista vertical optimizada (LazyColumn) para renderizar las tarjetas de clases
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(clases) { clase ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GrisTarjeta),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClaseClick(clase) } // Acción al hacer clic en la tarjeta
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ícono decorativo dentro de un cuadro con bordes redondeados
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(VerdeClaroBg, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = VerdeTecsup)
                        }
                        Spacer(modifier = Modifier.width(16.dp))

                        // Información reducida de la clase (nombre, hora y sala)
                        Column {
                            Text(clase.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${clase.hora} · ${clase.sala}", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}