package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// ViewModel de Mis citas: separa las citas del dueno en Proximas e Historial y aplica la regla
// de cancelacion. Solo conoce el Repository (interface). El dueno solo ve las citas de SUS mascotas
class MisCitasViewModel(
    private val repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    private companion object {
        // Solo se puede cancelar si faltan mas de 2 horas para la cita
        val ANTICIPACION_MINIMA: Duration = Duration.ofHours(2)
    }

    // Citas del dueno (Flow de Room: cuando cambian en la base, la lista se actualiza sola)
    private val citas: Flow<List<CitaConDetalle>> = repo.observarCitasDeDueno(duenoId)

    // Proximas: PENDIENTE y CONFIRMADA desde ahora, la mas cercana primero
    val proximas: StateFlow<List<CitaConDetalle>> = citas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { esProxima(it, ahora) }.sortedBy { fechaHora(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Historial: ATENDIDA y CANCELADA, y las que ya pasaron, la mas reciente primero
    val historial: StateFlow<List<CitaConDetalle>> = citas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { !esProxima(it, ahora) }.sortedByDescending { fechaHora(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Mensaje para el usuario (por ejemplo, ya no se puede cancelar)
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // Une la fecha (aaaa-MM-dd) y la hora (HH:mm) de la cita en un solo valor comparable
    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))

    // Una cita es proxima si esta PENDIENTE o CONFIRMADA y su fecha y hora no han pasado
    private fun esProxima(c: CitaConDetalle, ahora: LocalDateTime): Boolean {
        val estado = EstadoCita.desdeTexto(c.cita.estado)
        return (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) &&
                !fechaHora(c).isBefore(ahora)
    }

    // Regla de cancelacion: solo PENDIENTE o CONFIRMADA y solo si faltan mas de 2 horas
    fun puedeCancelar(c: CitaConDetalle): Boolean {
        val estado = EstadoCita.desdeTexto(c.cita.estado)
        if (estado != EstadoCita.PENDIENTE && estado != EstadoCita.CONFIRMADA) return false
        val faltan = Duration.between(LocalDateTime.now(), fechaHora(c))
        return faltan > ANTICIPACION_MINIMA
    }

    // Cancela la cita. La regla se vuelve a comprobar aqui por si el tiempo paso con el dialogo abierto
    fun cancelar(c: CitaConDetalle) {
        if (!puedeCancelar(c)) {
            _mensaje.value = "Ya no puedes cancelar esta cita"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA)
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}