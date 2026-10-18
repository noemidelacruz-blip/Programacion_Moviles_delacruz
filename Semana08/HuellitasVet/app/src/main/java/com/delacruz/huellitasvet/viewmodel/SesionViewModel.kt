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

// ViewModel de la sesion: valida el login, recuerda el perfil en DataStore y expone al usuario actual.
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

    // Modo oscuro guardado en DataStore (lo usan todas las pantallas)
    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

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
                // El rol de la cuenta no coincide con la pestaña elegida
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

    // Quita el mensaje de error (al escribir o cambiar de pestaña)
    fun limpiarError() {
        _error.value = null
    }

    // Cierra la sesion. El usuario se borra de inmediato y la preferencia se limpia despues,
    // asi el login no ve un usuario viejo y no navega de vuelta
    fun cerrarSesion() {
        _usuarioActual.value = null
        viewModelScope.launch { prefs.cerrarSesion() }
    }
}