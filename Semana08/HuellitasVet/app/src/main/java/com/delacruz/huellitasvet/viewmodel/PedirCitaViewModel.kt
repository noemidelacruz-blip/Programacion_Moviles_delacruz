package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.delacruz.huellitasvet.data.prefs.PreferenciasStore

// ViewModel de Pedir cita: guarda lo que el dueno va eligiendo (mascota, servicio, fecha y hora)
// y valida todo antes de crear la cita. Solo conoce el Repository (interface)
@OptIn(ExperimentalCoroutinesApi::class)
class PedirCitaViewModel(
    private val repo: VetRepository,
    duenoId: Int,
    mascotaInicial: Int,
    prefs: PreferenciasStore
) : ViewModel() {

    private companion object {
        // Se puede pedir cita desde hoy hasta 7 dias despues
        const val DIAS_MAXIMOS = 7
    }

    // Mascotas del dueno: solo puede pedir citas para las suyas
    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Solo servicios ACTIVOS: los inactivos no aparecen al pedir cita
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Si el veterinario cerro la agenda (Aceptar citas apagado), el dueno no puede pedir citas
    val aceptaCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Fechas que muestran los chips: hoy y los 7 dias siguientes
    val fechas: List<LocalDate> = List(DIAS_MAXIMOS + 1) { LocalDate.now().plusDays(it.toLong()) }

    // ---------- Lo que el dueno ha elegido ----------
    // Si se llega desde la ficha de una mascota, esa mascota ya viene elegida
    private val _mascotaId = MutableStateFlow<Int?>(mascotaInicial.takeIf { it != 0 })
    val mascotaId: StateFlow<Int?> = _mascotaId.asStateFlow()

    private val _servicioId = MutableStateFlow<Int?>(null)
    val servicioId: StateFlow<Int?> = _servicioId.asStateFlow()

    // Por defecto la fecha es hoy
    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    // Horas ocupadas de la fecha elegida: flatMapLatest cambia de consulta cada vez que cambia la fecha
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---------- Mensajes y eventos ----------
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // true cuando la cita ya se creo (la pantalla vuelve a Mis citas)
    private val _creada = MutableStateFlow(false)
    val creada: StateFlow<Boolean> = _creada.asStateFlow()

    fun elegirMascota(id: Int) {
        _mascotaId.value = id
    }

    fun elegirServicio(id: Int) {
        _servicioId.value = id
    }

    // Si la fecha cambia, se limpia la hora elegida
    fun elegirFecha(fecha: LocalDate) {
        if (fecha != _fecha.value) {
            _fecha.value = fecha
            _hora.value = null
        }
    }

    fun elegirHora(hora: String) {
        _hora.value = hora
    }

    // Si la fecha es hoy, las horas que ya pasaron no se pueden elegir
    fun esHoraPasada(fecha: LocalDate, hora: String): Boolean =
        fecha == LocalDate.now() && Horarios.esPasado(fecha.toString(), hora)

    // Valida todo y crea la cita. Las reglas viven aqui, no en la pantalla
    fun confirmar() {
        // Regla: si la clinica no acepta citas, no se crea ninguna
        if (!aceptaCitas.value) {
            _mensaje.value = "La clínica no está recibiendo citas"
            return
        }
        val mascotaElegida = _mascotaId.value
        val servicioElegido = _servicioId.value
        val horaElegida = _hora.value
        val fechaElegida = _fecha.value

        if (mascotaElegida == null || servicioElegido == null || horaElegida == null) {
            _mensaje.value = "Elige la mascota, el servicio y la hora"
            return
        }
        // La mascota debe ser del dueno
        if (mascotas.value.none { it.id == mascotaElegida }) {
            _mensaje.value = "Elige una de tus mascotas"
            return
        }
        // El servicio debe seguir activo
        if (servicios.value.none { it.id == servicioElegido }) {
            _mensaje.value = "Ese servicio ya no está disponible"
            _servicioId.value = null
            return
        }
        // Fecha desde hoy hasta 7 dias
        val hoy = LocalDate.now()
        if (fechaElegida.isBefore(hoy) || fechaElegida.isAfter(hoy.plusDays(DIAS_MAXIMOS.toLong()))) {
            _mensaje.value = "Elige una fecha dentro de los próximos $DIAS_MAXIMOS días"
            return
        }
        // Horario valido (09:00 a 17:30 cada 30 minutos) y que no haya pasado
        if (horaElegida !in Horarios.todos) {
            _mensaje.value = "Elige un horario válido"
            return
        }
        if (Horarios.esPasado(fechaElegida.toString(), horaElegida)) {
            _mensaje.value = "Esa hora ya pasó. Elige otra"
            _hora.value = null
            return
        }

        viewModelScope.launch {
            // El Repository comprueba otra vez que el horario siga libre y devuelve -1 si no
            val id = repo.crearCita(
                mascotaId = mascotaElegida,
                servicioId = servicioElegido,
                fecha = fechaElegida.toString(),
                hora = horaElegida,
                nota = ""
            )
            if (id == -1L) {
                _mensaje.value = "Ese horario se ocupó mientras tanto. Elige otro"
                _hora.value = null
            } else {
                _creada.value = true
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    // La pantalla ya reacciono al evento (volver a Mis citas)
    fun consumirEventos() {
        _creada.value = false
    }
}