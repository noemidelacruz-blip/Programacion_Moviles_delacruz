package com.delacruz.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.avisoscampus.data.AvisosRepository
import com.delacruz.avisoscampus.model.Aviso
import com.delacruz.avisoscampus.model.Origen
import com.delacruz.avisoscampus.model.PermisoUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    // Evento de una sola vez: "muestra esta notificación".
    // El ViewModel NO tiene Context: lo recibe la pantalla.
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private var trabajo: Job? = null

    // Segundos que faltan, o null si no hay recordatorio activo.
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion: StateFlow<Boolean> = _escuchandoAvion.asStateFlow()

    private val _escuchandoCargador = MutableStateFlow(false)
    val escuchandoCargador: StateFlow<Boolean> = _escuchandoCargador.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
        if (concedido) _mensaje.value = null
    }

    fun avisarAhora() {
        viewModelScope.launch {
            val aviso = repo.registrar("Tarea lista", "Tu entrega fue registrada", Origen.BOTON)
            _eventos.emit(aviso)
        }
    }

    fun recordarEn(segundos: Int = 10) {
        if (trabajo?.isActive == true) return

        trabajo = viewModelScope.launch {
            try {
                for (restantes in segundos downTo 1) {
                    _faltan.value = restantes
                    delay(1000)
                }
                val aviso = repo.registrar(
                    "Recordatorio",
                    "Revisa tu laboratorio pendiente",
                    Origen.RECORDATORIO
                )
                _eventos.emit(aviso)
            } finally {
                // Se ejecuta tanto si termina como si se cancela.
                _faltan.value = null
            }
        }
    }

    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }

    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }

    fun alCambiarModoAvion(activo: Boolean) {
        viewModelScope.launch {
            val texto = if (activo) "Activado" else "Desactivado"
            val aviso = repo.registrar("Modo avión", texto, Origen.MODO_AVION)
            _eventos.emit(aviso)
        }
    }

    fun cambiarEscuchaCargador(valor: Boolean) {
        _escuchandoCargador.value = valor
    }

    fun alCambiarCargador(conectado: Boolean) {
        viewModelScope.launch {
            val texto = if (conectado) "Conectado" else "Desconectado"
            val aviso = repo.registrar("Cargador", texto, Origen.CARGADOR)
            _eventos.emit(aviso)
        }
    }

    fun noSePudoMostrar() {
        _mensaje.value = "Sin permiso: el aviso se guardó en el historial, pero no se mostró"
    }
}
