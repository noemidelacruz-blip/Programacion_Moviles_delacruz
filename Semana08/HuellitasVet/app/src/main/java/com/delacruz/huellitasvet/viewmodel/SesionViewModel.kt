package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.data.prefs.PreferenciasStore
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel de la sesion: valida el login, recuerda el perfil en DataStore, expone al usuario actual
// y los ajustes (modo oscuro, notificaciones, aceptar citas). Permite cambiar de perfil.
// Solo conoce el Repository (interface) y las preferencias, nunca el DAO ni las pantallas
class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    // Usuario con sesion iniciada (null si no hay)
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    // Mensaje de error del login (null si no hay error)
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // true mientras se revisa si habia una sesion guardada al abrir la app
    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Ajustes guardados en DataStore (los usan todas las pantallas)
    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Ultimo dueno que tuvo la sesion, para volver a el al cambiar de perfil
    private var ultimoDueno: Usuario? = null

    init {
        restaurarSesion()
    }

    // Si DataStore tiene un perfil y un usuario guardados, recupera al usuario de Room
    private fun restaurarSesion() {
        viewModelScope.launch {
            val perfil = prefs.perfilActivo.first()
            val id = prefs.usuarioId.first()
            if (perfil.isNotEmpty() && id != 0) {
                val usuario = repo.obtenerUsuario(id)
                if (usuario != null) {
                    _usuarioActual.value = usuario
                } else {
                    // El usuario guardado ya no existe: se limpia la sesion
                    prefs.cerrarSesion()
                }
            }
            _cargando.value = false
        }
    }

    // Valida los datos y, si todo esta bien, guarda el perfil y deja al usuario como actual
    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        // Campos vacios
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim().lowercase(), clave)
            when {
                // Credenciales incorrectas
                usuario == null ->
                    _error.value = "Correo o contraseña incorrectos"
                // El rol de la cuenta no coincide con la pestana elegida
                Rol.desdeTexto(usuario.rol) != rolSeleccionado ->
                    _error.value = "Esta cuenta no corresponde al perfil ${rolSeleccionado.etiqueta}"
                // Exito: se guarda el perfil en DataStore
                else -> {
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    _error.value = null
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    // Quita el mensaje de error (al escribir o cambiar de pestana)
    fun limpiarError() {
        _error.value = null
    }

    // Cierra la sesion. El usuario se borra de inmediato y la preferencia se limpia despues,
    // asi el login no ve un usuario viejo y no navega de vuelta
    fun cerrarSesion() {
        _usuarioActual.value = null
        viewModelScope.launch { prefs.cerrarSesion() }
    }

    // Cambia al otro perfil (modo practica): guarda el nuevo perfil en DataStore y avisa a la
    // navegacion con el usuario nuevo para que vaya a su pantalla principal
    fun cambiarPerfil(onListo: (Usuario) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val rolActual = Rol.desdeTexto(actual.rol)
        if (rolActual == Rol.CLIENTE) ultimoDueno = actual
        val otroRol = if (rolActual == Rol.ADMIN) Rol.CLIENTE else Rol.ADMIN

        viewModelScope.launch {
            // Al volver al dueno se usa el ultimo que tuvo la sesion; si no hay, el primero de ese perfil
            val otro = (if (otroRol == Rol.CLIENTE) ultimoDueno else null)
                ?: repo.obtenerUsuarioPorRol(otroRol)
                ?: return@launch
            prefs.guardarSesion(otroRol, otro.id)
            _usuarioActual.value = otro
            onListo(otro)
        }
    }

    // ---------- Ajustes ----------
    fun cambiarModoOscuro(activado: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(activado) }
    }

    fun cambiarNotificaciones(activado: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(activado) }
    }

    fun cambiarAceptarCitas(activado: Boolean) {
        viewModelScope.launch { prefs.guardarAceptarCitas(activado) }
    }
}