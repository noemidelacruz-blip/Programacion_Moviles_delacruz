package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Errores del formulario de servicio, uno por campo (null = el campo esta bien)
data class ErroresServicio(
    val nombre: String? = null,
    val duracion: String? = null,
    val precio: String? = null
) {
    val hayErrores: Boolean
        get() = nombre != null || duracion != null || precio != null
}

// ViewModel de los servicios: la lista, el activar y desactivar, y guardar y eliminar.
// Solo conoce el Repository (interface), no el DAO
class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    companion object {
        // Duraciones permitidas en el formulario (minutos)
        val DURACIONES = listOf(15, 30, 45, 60)
    }

    // Convierte el Flow de Room en StateFlow para que la pantalla lo observe.
    // WhileSubscribed(5000) mantiene la consulta viva 5 segundos si la pantalla se rota
    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Servicio que se esta editando en el formulario (null si es uno nuevo)
    private val _servicioEdicion = MutableStateFlow<Servicio?>(null)
    val servicioEdicion: StateFlow<Servicio?> = _servicioEdicion.asStateFlow()

    private val _errores = MutableStateFlow(ErroresServicio())
    val errores: StateFlow<ErroresServicio> = _errores.asStateFlow()

    // Mensaje para el usuario (por ejemplo, no se puede eliminar)
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // true cuando el servicio se guardo o se elimino (el formulario vuelve a la lista)
    private val _listo = MutableStateFlow(false)
    val listo: StateFlow<Boolean> = _listo.asStateFlow()

    // Activa o desactiva un servicio (el switch de la lista)
    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    // Carga el servicio a editar (id = 0 significa servicio nuevo)
    fun cargar(id: Int) {
        if (id == 0) return
        viewModelScope.launch { _servicioEdicion.value = repo.obtenerServicio(id) }
    }

    // Valida y guarda un servicio, nuevo (id = 0) o editado
    fun guardar(servicio: Servicio) {
        val errores = ErroresServicio(
            nombre = if (servicio.nombre.trim().isEmpty()) "Escribe el nombre del servicio" else null,
            duracion = if (servicio.duracionMin !in DURACIONES) "Elige una duración" else null,
            precio = if (servicio.precio <= 0.0) "Debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return

        viewModelScope.launch {
            repo.guardarServicio(
                servicio.copy(
                    nombre = servicio.nombre.trim(),
                    descripcion = servicio.descripcion.trim()
                )
            )
            _listo.value = true
        }
    }

    // Elimina un servicio solo si no tiene citas; si tiene, avisa y no lo borra (solo se desactiva)
    fun eliminar(servicio: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(servicio.id)) {
                _mensaje.value =
                    "No se puede eliminar ${servicio.nombre}: tiene citas. Puedes desactivarlo"
            } else {
                repo.eliminarServicio(servicio)
                _listo.value = true
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    // La pantalla ya reacciono al evento (volver a la lista)
    fun consumirListo() {
        _listo.value = false
    }
}