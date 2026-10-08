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
import androidx.compose.runtime.remember
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
import com.delacruz.saludpluscitas.ui.components.mesYAnio
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.FondoClaro
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario
import java.time.DayOfWeek
import java.time.LocalDate

// Día del calendario: etiqueta corta, número y fecha ISO que viaja en la ruta.
private data class DiaCalendario(
    val etiqueta: String,
    val numero: String,
    val fecha: String
)

// Etiquetas cortas en el orden de DayOfWeek (lunes = 1 ... domingo = 7).
private val etiquetasDia = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

// Cantidad de días hábiles que muestra el calendario.
private const val DIAS_VISIBLES = 5

// Fase 2: devuelve los primeros "cantidad" días hábiles (lunes a viernes)
// a partir de la fecha indicada, incluyéndola si es un día hábil.
private fun diasHabiles(desde: LocalDate, cantidad: Int): List<LocalDate> {
    val resultado = mutableListOf<LocalDate>()
    var dia = desde
    while (resultado.size < cantidad) {
        if (dia.dayOfWeek != DayOfWeek.SATURDAY && dia.dayOfWeek != DayOfWeek.SUNDAY) {
            resultado.add(dia)
        }
        dia = dia.plusDays(1)
    }
    return resultado
}

// Convierte un LocalDate en el día que se dibuja en el calendario.
private fun LocalDate.aDiaCalendario(): DiaCalendario {
    return DiaCalendario(
        etiqueta = etiquetasDia[dayOfWeek.value - 1],
        numero = dayOfMonth.toString(),
        fecha = toString()
    )
}

@Composable
fun FechaHoraScreen(
    navController: NavController,
    medicoId: Int
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    // Fase 2: los días se calculan a partir de la fecha de hoy.
    val hoy = remember { LocalDate.now() }
    val fechasVisibles = remember(hoy) { diasHabiles(hoy, DIAS_VISIBLES) }
    val dias = fechasVisibles.map { it.aDiaCalendario() }

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

            // Las flechas se activan en el siguiente commit de la Fase 2.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}, enabled = false) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                }
                Text(
                    // El mes y el año salen del primer día que se muestra.
                    text = mesYAnio(fechasVisibles.first()),
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