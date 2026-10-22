package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Errores del formulario de atencion, uno por campo (null = el campo esta bien)
data class ErroresAtencion(
    val diagnostico: String? = null,
    val peso: String? = null
) {
    val hayErrores: Boolean
        get() = diagnostico != null || peso != null
}

// ViewModel de Atender cita: muestra la cita elegida, valida el registro clinico y lo guarda.
// Solo conoce el Repository (interface), nunca el DAO ni las pantallas
class AtenderCitaViewModel(
    private val repo: VetRepository,
    private val citaId: Int
) : ViewModel() {

    // La cita con su mascota, su dueno y su servicio
    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errores = MutableStateFlow(ErroresAtencion())
    val errores: StateFlow<ErroresAtencion> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // true cuando la atencion se registro o la cita se cancelo (la pantalla vuelve a la Agenda)
    private val _listo = MutableStateFlow(false)
    val listo: StateFlow<Boolean> = _listo.asStateFlow()

    // Regla: solo se puede atender una cita PENDIENTE o CONFIRMADA
    fun puedeAtender(c: CitaConDetalle): Boolean {
        val estado = EstadoCita.desdeTexto(c.cita.estado)
        return estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA
    }

    // Valida los campos y registra la atencion. El Repository guarda diagnostico, tratamiento y
    // peso en la cita, la pasa a ATENDIDA y actualiza el peso de la mascota en UNA transaccion
    fun registrar(diagnostico: String, tratamiento: String, peso: String) {
        // Se acepta coma o punto como separador decimal
        val pesoNumero = peso.trim().replace(',', '.').toDoubleOrNull()

        val errores = ErroresAtencion(
            diagnostico = if (diagnostico.trim().length < 5) "Escribe al menos 5 caracteres" else null,
            peso = if (pesoNumero == null || pesoNumero <= 0.0) "Debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores || pesoNumero == null) return

        viewModelScope.launch {
            // Se vuelve a leer la cita por si su estado cambio mientras se escribia
            val actual = repo.observarCita(citaId).first()
            if (actual == null || !puedeAtender(actual)) {
                _mensaje.value = "Solo se puede atender una cita pendiente o confirmada"
                return@launch
            }
            repo.registrarAtencion(
                citaId = citaId,
                diagnostico = diagnostico.trim(),
                tratamiento = tratamiento.trim().ifBlank { "Ninguno" },
                peso = pesoNumero
            )
            _listo.value = true
        }
    }

    // Cancela la cita (desde PENDIENTE o CONFIRMADA)
    fun cancelar() {
        viewModelScope.launch {
            val actual = repo.observarCita(citaId).first()
            if (actual == null || !puedeAtender(actual)) {
                _mensaje.value = "Solo se puede cancelar una cita pendiente o confirmada"
                return@launch
            }
            repo.cambiarEstadoCita(citaId, EstadoCita.CANCELADA)
            _listo.value = true
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    // La pantalla ya reacciono al evento (volver a la Agenda)
    fun consumirListo() {
        _listo.value = false
    }
}