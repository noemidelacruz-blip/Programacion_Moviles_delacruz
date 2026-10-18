package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// ViewModel del Inicio del dueno: sus mascotas y su proxima cita.
// Solo conoce el Repository (interface). El dueno solo ve SUS mascotas y SUS citas
class InicioViewModel(
    private val repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    // Mascotas del dueno (carrusel del Inicio y pantalla Mascotas)
    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Proxima cita: la primera PENDIENTE o CONFIRMADA con fecha y hora desde ahora,
    // calculada a partir de las citas del dueno
    val proximaCita: StateFlow<CitaConDetalle?> = repo.observarCitasDeDueno(duenoId)
        .map { citas ->
            val ahora = LocalDateTime.now()
            citas
                .filter { c ->
                    val estado = EstadoCita.desdeTexto(c.cita.estado)
                    (estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA) &&
                            !fechaHora(c).isBefore(ahora)
                }
                .minByOrNull { fechaHora(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Une la fecha (aaaa-MM-dd) y la hora (HH:mm) de la cita en un solo valor comparable
    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))
}