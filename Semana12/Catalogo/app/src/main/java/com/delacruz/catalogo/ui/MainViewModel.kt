package com.delacruz.catalogo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.delacruz.catalogo.data.auth.AuthRepository
import com.delacruz.catalogo.domain.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel principal (hito H13).
 *
 * Se encarga de la sesión: al abrir la app pregunta a Firebase si hay una
 * sesión guardada (R11) y expone si la persona entró o no, para que la
 * navegación decida qué mostrar.
 */
class MainViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            _estado.value = UiState.Exito(auth.sesionActual())
        }
    }

    /** Guarda la sesión que devolvió el login. */
    fun actualizarSesion(sesion: Sesion?) {
        _estado.value = UiState.Exito(sesion)
    }

    /** Cierra la sesión en Firebase y vuelve al login. */
    fun cerrarSesion() {
        auth.cerrarSesion()
        _estado.value = UiState.Exito(null)
    }
}

/** Factory que recibe el AuthRepository. */
class MainViewModelFactory(
    private val auth: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(auth) as T
        }
        throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
