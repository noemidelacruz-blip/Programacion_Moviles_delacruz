package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.components.BotonPrimario
import com.delacruz.saludpluscitas.ui.components.ChipHorario
import com.delacruz.saludpluscitas.ui.components.EstadoVacio
import com.delacruz.saludpluscitas.ui.components.ResumenMedico
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.FondoClaro
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

// Día del calendario: etiqueta corta, número y fecha ISO que viaja en la ruta.
private data class DiaCalendario(
    val etiqueta: String,
    val numero: String,
    val fecha: String
)

// Fase 1: semana fija de lunes a viernes. En la Fase 2 se generan con LocalDate.
private const val MES = "Octubre 2026"
private val dias = listOf(
    DiaCalendario("Lun", "12", "2026-10-12"),
    DiaCalendario("Mar", "13", "2026-10-13"),
    DiaCalendario("Mié", "14", "2026-10-14"),
    DiaCalendario("Jue", "15", "2026-10-15"),
    DiaCalendario("Vie", "16", "2026-10-16")
)

@Composable
fun FechaHoraScreen(
    navController: NavController,
    medicoId: Int
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    // rememberSaveable: al volver de Confirmar se mantiene lo elegido.
    var fecha by rememberSaveable { mutableStateOf<String?>(null) }
    var hora by rememberSaveable { mutableStateOf<String?>(null) }

    // Sin día elegido no se muestran horas; con día, solo las libres.
    val horarios = fecha?.let { Repositorio.horariosDisponibles(medicoId, it) } ?: emptyList()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Seleccionar fecha y hora",
                onAtras = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            medico?.let { ResumenMedico(medico = it) }

            Spacer(modifier = Modifier.height(16.dp))

            // Las flechas se activan en la Fase 2 (calendario dinámico).
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}, enabled = false) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                }
                Text(
                    text = MES,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {}, enabled = false) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dias.forEach { dia ->
                    val seleccionado = dia.fecha == fecha
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (seleccionado) AzulPrimario else FondoClaro)
                            .clickable {
                                fecha = dia.fecha
                                // La hora elegida puede no existir en el nuevo día.
                                hora = null
                            }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dia.etiqueta,
                            fontSize = 13.sp,
                            color = if (seleccionado) Color.White else TextoSecundario
                        )
                        Text(
                            text = dia.numero,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (seleccionado) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when {
                fecha == null -> EstadoVacio(
                    mensaje = "Elige un día para ver los horarios",
                    icono = Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f)
                )
                horarios.isEmpty() -> EstadoVacio(
                    mensaje = "No hay horarios disponibles este día",
                    modifier = Modifier.weight(1f)
                )
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(horarios, key = { it }) { h ->
                        ChipHorario(
                            hora = h,
                            seleccionado = h == hora,
                            onClick = { hora = h }
                        )
                    }
                }
            }

            BotonPrimario(
                texto = "Continuar",
                enabled = fecha != null && hora != null,
                onClick = {
                    val f = fecha
                    val h = hora
                    if (f != null && h != null) {
                        navController.navigate(Rutas.ConfirmarCita.crearRuta(medicoId, f, h))
                    }
                },
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}