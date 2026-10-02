package com.delacruz.tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FilaEjercicio(
    val nombre: String,
    val series: String,
    val repsTiempo: String,
    val descanso: String
)

data class NivelRutina(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val duracion: String,
    val frecuencia: String,
    val color: Color,
    val ejercicios: List<FilaEjercicio>
)

@Composable
fun PantallaRutinas() {
    // Estado para guardar el nivel seleccionado (null = lista de 3 niveles)
    var nivelSeleccionado by remember { mutableStateOf<NivelRutina?>(null) }

    val listaNiveles = remember {
        listOf(
            NivelRutina(
                id = "1",
                titulo = "Principiante / Básico",
                descripcion = "Adaptación anatómica, postura y control corporal.",
                duracion = "30-40 min",
                frecuencia = "3 días / semana",
                color = Color(0xFF2E7D32), // Verde
                ejercicios = listOf(
                    FilaEjercicio("Sentadillas asistidas", "3", "10 - 12", "60s"),
                    FilaEjercicio("Flexiones en pared/rodillas", "3", "8 - 10", "60s"),
                    FilaEjercicio("Plancha abdominal", "3", "20 - 30s", "45s"),
                    FilaEjercicio("Puente de glúteo", "3", "12 - 15", "45s")
                )
            ),
            NivelRutina(
                id = "2",
                titulo = "Intermedio",
                descripcion = "Fuerza funcional, tono muscular y ejercicios compuestos.",
                duracion = "45-50 min",
                frecuencia = "4 días / semana",
                color = Color(0xFF0288D1), // Azul
                ejercicios = listOf(
                    FilaEjercicio("Zancadas con mancuernas", "4", "12 / pierna", "60s"),
                    FilaEjercicio("Press militar de hombros", "4", "10 - 12", "60s"),
                    FilaEjercicio("Remo con mancuerna", "4", "12", "60s"),
                    FilaEjercicio("Mountain Climbers", "3", "40s", "45s")
                )
            ),
            NivelRutina(
                id = "3",
                titulo = "Avanzado",
                descripcion = "Circuitos de alta intensidad (HIIT) y máxima potencia.",
                duracion = "60 min",
                frecuencia = "5 días / semana",
                color = Color(0xFFD32F2F), // Rojo
                ejercicios = listOf(
                    FilaEjercicio("Sentadilla con salto + peso", "4", "15", "45s"),
                    FilaEjercicio("Burpees completos", "4", "12", "45s"),
                    FilaEjercicio("Dominadas / Remo pesado", "4", "8 - 10", "60s"),
                    FilaEjercicio("Plancha dinámica", "4", "60s", "30s")
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
            .padding(16.dp)
    ) {
        if (nivelSeleccionado == null) {
            // VISTA 1: LISTA DE NIVELES
            Text(
                text = "Selecciona tu Nivel",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(listaNiveles) { nivel ->
                    TarjetaNivel(nivel = nivel, onClick = { nivelSeleccionado = nivel })
                }
            }
        } else {
            // VISTA 2: DETALLE Y TABLA DEL NIVEL SELECCIONADO
            val nivel = nivelSeleccionado!!

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { nivelSeleccionado = null }
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Volver a niveles",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }

            // Título del Nivel
            Text(
                text = nivel.titulo,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = nivel.color,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Tabla de ejercicios
            TablaEjercicios(nivel = nivel)
        }
    }
}

@Composable
fun TarjetaNivel(nivel: NivelRutina, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(50.dp)
                    .background(nivel.color, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nivel.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF111827)
                )
                Text(
                    text = nivel.descripcion,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${nivel.duracion} • ${nivel.frecuencia}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = nivel.color
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ir",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun TablaEjercicios(nivel: NivelRutina) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
        ) {
            // Encabezado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F4F6))
                    .padding(vertical = 10.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ejercicio", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                Text("Series", fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(0.8f))
                Text("Reps/Tiempo", fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1.2f))
                Text("Descanso", fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            }

            Divider(color = Color(0xFFE5E7EB), thickness = 1.dp)

            // Filas
            nivel.ejercicios.forEachIndexed { index, ej ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (index % 2 == 0) Color.White else Color(0xFFFAFAFA))
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(ej.nombre, fontSize = 12.sp, color = Color(0xFF1F2937), modifier = Modifier.weight(2f))
                    Text(ej.series, fontSize = 12.sp, color = Color(0xFF4B5563), textAlign = TextAlign.Center, modifier = Modifier.weight(0.8f))
                    Text(ej.repsTiempo, fontSize = 12.sp, color = Color(0xFF4B5563), textAlign = TextAlign.Center, modifier = Modifier.weight(1.2f))
                    Text(ej.descanso, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = nivel.color, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                }

                if (index < nivel.ejercicios.size - 1) {
                    Divider(color = Color(0xFFF3F4F6), thickness = 1.dp)
                }
            }
        }
    }
}