package com.delacruz.catalogo.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.delacruz.catalogo.data.auth.AuthRepository
import com.delacruz.catalogo.domain.model.Sesion
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado del formulario de login (hito H13).
 *
 * Incluye lo que escribe la persona, si está cargando, el error a mostrar y la
 * sesión cuando el ingreso fue correcto.
 */
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val sesion: Sesion? = null
)

/**
 * ViewModel del login.
 *
 * Valida los campos ANTES de llamar a Firebase (R01/PA-04) y traduce los
 * errores de Firebase a mensajes claros (PA-03).
 */
class LoginViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    fun actualizarCorreo(valor: String) {
        _estado.update { it.copy(correo = valor, error = null) }
    }

    fun actualizarClave(valor: String) {
        _estado.update { it.copy(clave = valor, error = null) }
    }

    /**
     * Limpia la sesión y los campos una vez que la navegación ya la recibió.
     * Así al cerrar sesión el formulario vuelve vacío.
     */
    fun sesionConsumida() {
        _estado.value = LoginUiState()
    }

    fun ingresar() {
        val actual = _estado.value
        if (actual.cargando) return

        val correo = actual.correo.trim()

        // Validación local: si falta algo, no se llama a Firebase.
        if (correo.isEmpty() || actual.clave.isEmpty()) {
            _estado.update { it.copy(error = "Ingresa tu correo y tu contraseña") }
            return
        }
        if (!correo.contains("@") || !correo.contains(".")) {
            _estado.update { it.copy(error = "Ingresa un correo válido") }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            auth.iniciarSesion(correo, actual.clave)
                .onSuccess { sesion ->
                    _estado.update { it.copy(cargando = false, sesion = sesion) }
                }
                .onFailure { error ->
                    _estado.update { it.copy(cargando = false, error = mensajeDe(error)) }
                }
        }
    }

    private fun mensajeDe(error: Throwable): String = when (error) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> "Correo o contraseña incorrectos"

        is FirebaseAuthException -> "No se pudo iniciar sesión. Verifica tus datos."

        else -> "No se pudo iniciar sesión. Revisa tu conexión a internet."
    }
}

/** Factory que recibe el AuthRepository. */
class LoginViewModelFactory(
    private val auth: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(auth) as T
        }
        throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
