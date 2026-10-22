package com.delacruz.huellitasvet

import android.app.Application
import com.delacruz.huellitasvet.data.local.DatosIniciales
import com.delacruz.huellitasvet.data.local.VetDatabase
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.data.repository.VetRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

import com.delacruz.huellitasvet.data.prefs.PreferenciasStore

// Clase Application: crea la base de datos y el repositorio una sola vez para toda la app
class VetApp : Application() {

    // La base se crea cuando se necesita por primera vez
    val database: VetDatabase by lazy { VetDatabase.obtener(this) }

    // El repositorio que usan los ViewModels (se expone como interface)
    val repository: VetRepository by lazy { VetRepositoryRoom(database) }

    // Preferencias guardadas en DataStore (perfil activo, modo oscuro, etc.)
    val preferencias: PreferenciasStore by lazy { PreferenciasStore(this) }

    override fun onCreate() {
        super.onCreate()
        // Siembra los datos iniciales en segundo plano (Dispatchers.IO)
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}