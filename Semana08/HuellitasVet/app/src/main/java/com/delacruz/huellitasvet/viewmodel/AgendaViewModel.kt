package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// ViewModel de la Agenda del veterinario: las citas del dia elegido y las acciones que cambian
// su estado. Solo conoce el Repository (interface). El veterinario ve todas las citas
@OptIn(ExperimentalCoroutinesApi::class)
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {

    // Dias que muestran los chips: hoy y los 6 siguientes
    val dias: List<LocalDate> = List(7) { LocalDate.now().plusDays(it.toLong()) }

    // Por defecto la fecha es hoy
    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now())
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    // Citas del dia elegido ordenadas por hora. flatMapLatest cambia de consulta con cada fecha
    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { repo.observarCitasPorFecha(it.toString()) }
        .map { lista -> lista.sortedBy { it.cita.hora } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirFecha(fecha: LocalDate) {
        _fechaSeleccionada.value = fecha
    }

    // Regla de estados: solo una cita PENDIENTE pasa a CONFIRMADA
    fun confirmar(c: CitaConDetalle) {
        if (EstadoCita.desdeTexto(c.cita.estado) != EstadoCita.PENDIENTE) return
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CONFIRMADA) }
    }

    // Regla de estados: desde PENDIENTE o CONFIRMADA se puede cancelar. ATENDIDA y CANCELADA son finales
    fun cancelar(c: CitaConDetalle) {
        val estado = EstadoCita.desdeTexto(c.cita.estado)
        if (estado != EstadoCita.PENDIENTE && estado != EstadoCita.CONFIRMADA) return
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA) }
    }
}