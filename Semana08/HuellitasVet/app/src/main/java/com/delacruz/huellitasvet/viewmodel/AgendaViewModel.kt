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
import java.time.YearMonth

// ViewModel de la Agenda del veterinario: las citas del dia elegido, el calendario del mes y
// las acciones que cambian el estado de una cita. Solo conoce el Repository (interface)
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

    // Mes que muestra el calendario (empieza en el mes actual)
    private val _mesVisible = MutableStateFlow(YearMonth.now())
    val mesVisible: StateFlow<YearMonth> = _mesVisible.asStateFlow()

    // false = vista de semana (chips), true = vista de mes (calendario)
    private val _vistaMes = MutableStateFlow(false)
    val vistaMes: StateFlow<Boolean> = _vistaMes.asStateFlow()

    // Citas no canceladas de cada dia del mes visible, como mapa de "aaaa-MM-dd" a total.
    // flatMapLatest vuelve a consultar cada vez que cambia el mes
    val diasConCitas: StateFlow<Map<String, Int>> = _mesVisible
        .flatMapLatest { mes ->
            repo.observarCitasPorDia(
                inicio = mes.atDay(1).toString(),
                fin = mes.atEndOfMonth().toString()
            )
        }
        .map { lista -> lista.associate { it.fecha to it.total } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun elegirFecha(fecha: LocalDate) {
        _fechaSeleccionada.value = fecha
    }

    fun mesAnterior() {
        _mesVisible.value = _mesVisible.value.minusMonths(1)
    }

    fun mesSiguiente() {
        _mesVisible.value = _mesVisible.value.plusMonths(1)
    }

    // Vuelve al mes actual y elige hoy
    fun irAHoy() {
        _mesVisible.value = YearMonth.now()
        _fechaSeleccionada.value = LocalDate.now()
    }

    fun alternarVista() {
        _vistaMes.value = !_vistaMes.value
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
