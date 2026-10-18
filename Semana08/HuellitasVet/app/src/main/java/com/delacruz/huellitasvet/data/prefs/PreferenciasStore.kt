package com.delacruz.huellitasvet.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.delacruz.huellitasvet.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// DataStore declarado una sola vez, a nivel de archivo (el archivo se llama "ajustes").
// Sirve para preferencias pequenas (clave-valor). Las mascotas, citas y servicios van en Room
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

// Guarda en el dispositivo el perfil activo y los ajustes de la app.
// Cada preferencia se lee con un Flow y se escribe con una funcion suspend
class PreferenciasStore(private val context: Context) {

    // Claves de las preferencias
    private object Claves {
        val PERFIL_ACTIVO = stringPreferencesKey("perfilActivo")
        val USUARIO_ID = intPreferencesKey("usuarioId")
        val MODO_OSCURO = booleanPreferencesKey("modoOscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val ACEPTAR_CITAS = booleanPreferencesKey("aceptarCitas")
    }

    // Lectura base: si el archivo no se puede leer, se usan valores vacios en vez de fallar
    private val datos: Flow<Preferences> = context.dataStore.data.catch { error ->
        if (error is IOException) emit(emptyPreferences()) else throw error
    }

    // ---------- Lectura (un Flow por cada preferencia) ----------
    // Perfil guardado: "CLIENTE", "ADMIN" o vacio si no hay sesion
    val perfilActivo: Flow<String> = datos.map { it[Claves.PERFIL_ACTIVO] ?: "" }

    // Id del usuario con sesion iniciada (0 si no hay)
    val usuarioId: Flow<Int> = datos.map { it[Claves.USUARIO_ID] ?: 0 }

    val modoOscuro: Flow<Boolean> = datos.map { it[Claves.MODO_OSCURO] ?: false }

    // Por defecto activadas
    val notificaciones: Flow<Boolean> = datos.map { it[Claves.NOTIFICACIONES] ?: true }

    // Si esta apagado, el dueno no puede pedir citas
    val aceptarCitas: Flow<Boolean> = datos.map { it[Claves.ACEPTAR_CITAS] ?: true }

    // ---------- Escritura (suspend) ----------
    // Guarda el perfil y el usuario activos (al iniciar sesion o al cambiar de perfil)
    suspend fun guardarSesion(rol: Rol, usuarioId: Int) {
        context.dataStore.edit { prefs ->
            prefs[Claves.PERFIL_ACTIVO] = rol.name
            prefs[Claves.USUARIO_ID] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(activado: Boolean) {
        context.dataStore.edit { prefs -> prefs[Claves.MODO_OSCURO] = activado }
    }

    suspend fun guardarNotificaciones(activado: Boolean) {
        context.dataStore.edit { prefs -> prefs[Claves.NOTIFICACIONES] = activado }
    }

    suspend fun guardarAceptarCitas(activado: Boolean) {
        context.dataStore.edit { prefs -> prefs[Claves.ACEPTAR_CITAS] = activado }
    }

    // Borra solo la sesion. El modo oscuro y los demas ajustes se conservan
    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs.remove(Claves.PERFIL_ACTIVO)
            prefs.remove(Claves.USUARIO_ID)
        }
    }
}