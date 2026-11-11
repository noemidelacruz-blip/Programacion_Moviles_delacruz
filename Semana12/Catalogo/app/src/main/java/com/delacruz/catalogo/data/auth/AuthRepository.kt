package com.delacruz.catalogo.data.auth

import com.delacruz.catalogo.domain.model.Sesion

/**
 * Contrato de autenticación.
 *
 * El ViewModel depende de esta interface y nunca de Firebase directamente.
 * La implementación real (FirebaseAuthRepository) llega en el hito H13.
 */
interface AuthRepository {

    /** Inicia sesión con correo y contraseña y devuelve la sesión con su rol. */
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>

    /** Devuelve la sesión guardada por Firebase, o null si no hay sesión. */
    suspend fun sesionActual(): Sesion?

    /** Cierra la sesión actual. */
    fun cerrarSesion()
}
