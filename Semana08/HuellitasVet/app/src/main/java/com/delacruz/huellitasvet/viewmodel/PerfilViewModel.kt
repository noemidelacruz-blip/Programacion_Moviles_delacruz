package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// ViewModel del Perfil del dueno: los tres contadores (mascotas, citas proximas y atenciones).
// Solo conoce el Repository (interface). El dueno solo cuenta SUS mascotas y SUS citas
class PerfilViewModel(
    repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    private val citas = repo.observarCitasDeDueno(duenoId)

    // Cantidad de mascotas del dueno
    val mascotas: StateFlow<Int> = repo.observarMascotasDeDueno(duenoId)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Citas proximas: PENDIENTE o CONFIRMADA con fecha y hora desde ahora
    val proximas: StateFlow<Int> = citas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.count { c ->
                val estado = EstadoCita.desdeTexto(c.cita.estado)
                val fechaHora = LocalDateTime.of(
                    LocalDate.parse(c.cita.fecha),
                    LocalTime.parse(c.cita.hora)
                )
                (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) &&
                        !fechaHora.isBefore(ahora)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Atenciones: citas ATENDIDAS
    val atenciones: StateFlow<Int> = citas
        .map { lista -> lista.count { EstadoCita.desdeTexto(it.cita.estado) == EstadoCita.ATENDIDA } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}