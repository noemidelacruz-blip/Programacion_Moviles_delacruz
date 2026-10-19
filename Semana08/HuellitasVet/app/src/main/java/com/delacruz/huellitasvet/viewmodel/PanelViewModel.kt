package com.delacruz.huellitasvet.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.ui.components.graficos.DatoGrafico
import com.delacruz.huellitasvet.ui.theme.AzulPrimario
import com.delacruz.huellitasvet.ui.theme.EstadoAtendida
import com.delacruz.huellitasvet.ui.theme.EstadoCancelada
import com.delacruz.huellitasvet.ui.theme.EstadoConfirmada
import com.delacruz.huellitasvet.ui.theme.EstadoPendiente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

// ViewModel del Panel de la clinica: contadores, ingresos del mes y datos de los graficos.
// Solo conoce el Repository (interface). Convierte los datos de Room al formato DatoGrafico
class PanelViewModel(private val repo: VetRepository) : ViewModel() {

    private companion object {
        // Azul claro de las barras que no son la mas alta
        val AZUL_SUAVE = Color(0xFF93C5FD)
    }

    val hoy: LocalDate = LocalDate.now()

    // Dia 1 del mes: desde ahi se suman los ingresos
    private val inicioMes: String = hoy.withDayOfMonth(1).toString()

    // Convierte un Flow de Room en StateFlow para que la pantalla lo observe
    private fun <T> compartir(flujo: Flow<T>, inicial: T): StateFlow<T> =
        flujo.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), inicial)

    // ---------- Contadores ----------
    // Citas de hoy sin contar las canceladas
    val citasHoy: StateFlow<Int> = compartir(repo.observarCitasDeFecha(hoy.toString()), 0)

    val pendientes: StateFlow<Int> =
        compartir(repo.observarCantidadPorEstado(EstadoCita.PENDIENTE), 0)

    val atendidas: StateFlow<Int> =
        compartir(repo.observarCantidadPorEstado(EstadoCita.ATENDIDA), 0)

    // ---------- Ingresos del mes ----------
    // Suma del precio de las citas ATENDIDAS desde el dia 1 del mes
    val ingresosMes: StateFlow<Double> = compartir(repo.observarIngresosDesde(inicioMes), 0.0)

    // Cuantas citas ATENDIDAS hay en el mes
    val atencionesMes: StateFlow<Int> = compartir(repo.observarAtencionesDesde(inicioMes), 0)

    // ---------- Graficos ----------
    // Dona: una porcion por estado, con el color del chip de estado
    val porEstado: StateFlow<List<DatoGrafico>> = compartir(
        combine(
            repo.observarCantidadPorEstado(EstadoCita.PENDIENTE),
            repo.observarCantidadPorEstado(EstadoCita.CONFIRMADA),
            repo.observarCantidadPorEstado(EstadoCita.ATENDIDA),
            repo.observarCantidadPorEstado(EstadoCita.CANCELADA)
        ) { pendiente, confirmada, atendida, cancelada ->
            listOf(
                DatoGrafico("Pendiente", pendiente, EstadoPendiente),
                DatoGrafico("Confirmada", confirmada, EstadoConfirmada),
                DatoGrafico("Atendida", atendida, EstadoAtendida),
                DatoGrafico("Cancelada", cancelada, EstadoCancelada)
            )
        },
        emptyList()
    )

    // Barras: citas por servicio (maximo 4). La barra mas alta va en azul fuerte
    val porServicio: StateFlow<List<DatoGrafico>> = compartir(
        repo.observarCitasPorServicio().map { lista ->
            lista.take(4).mapIndexed { indice, s ->
                DatoGrafico(
                    etiqueta = etiquetaCorta(s.nombre),
                    valor = s.total,
                    color = if (indice == 0) AzulPrimario else AZUL_SUAVE
                )
            }
        },
        emptyList()
    )

    // Nombre corto para el eje del grafico: "Consulta general" -> "Consulta"
    private fun etiquetaCorta(nombre: String): String = when {
        nombre.startsWith("Vacun", ignoreCase = true) -> "Vacunas"
        else -> nombre.substringBefore(" ")
    }
}