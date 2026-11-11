package com.delacruz.catalogo.domain.model

/**
 * Roles de la aplicación.
 *
 * El rol NO se decide en la app: se guarda en Firestore (usuarios/{uid}.rol)
 * y se lee después de iniciar sesión.
 */
enum class Rol {
    ADMIN,
    USUARIO
}

/** Sesión activa del usuario que ingresó. */
data class Sesion(
    val uid: String,
    val correo: String,
    val rol: Rol
)
