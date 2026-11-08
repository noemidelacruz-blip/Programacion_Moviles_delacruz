package com.delacruz.catalogo.data.auth

import com.delacruz.catalogo.domain.model.Rol
import com.delacruz.catalogo.domain.model.Sesion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Implementación real de AuthRepository con Firebase (hito H12).
 *
 * - Authentication valida el correo y la contraseña.
 * - Firestore guarda el rol en usuarios/{uid}. La app NO decide el rol:
 *   lo lee de la base de datos después de ingresar.
 *
 * await() viene de kotlinx-coroutines-play-services.
 */
class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val usuario = auth.signInWithEmailAndPassword(correo, clave).await().user
            ?: error("Firebase no devolvió el usuario")
        Result.success(leerSesion(usuario.uid, usuario.email ?: correo))
    } catch (e: Exception) {
        // Credenciales inválidas, usuario inexistente, sin red, etc.
        Result.failure(e)
    }

    /**
     * Devuelve la sesión guardada por Firebase o null si no hay sesión.
     * Si el documento del rol no se puede leer, también devuelve null para
     * que la app vuelva al login en lugar de entrar con un rol inventado.
     */
    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return try {
            leerSesion(usuario.uid, usuario.email.orEmpty())
        } catch (e: Exception) {
            null
        }
    }

    override fun cerrarSesion() {
        auth.signOut()
    }

    /** Lee el documento usuarios/{uid} y traduce el campo rol al enum. */
    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val documento = db.collection("usuarios").document(uid).get().await()
        val rol = if (documento.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        return Sesion(uid = uid, correo = correo, rol = rol)
    }
}
