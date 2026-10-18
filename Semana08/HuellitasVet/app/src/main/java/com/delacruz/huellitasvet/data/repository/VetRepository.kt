package com.delacruz.huellitasvet.data.repository

import com.delacruz.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

// El contrato entre los ViewModels y los datos. Es una interface para que mas adelante
// se pueda conectar a un servicio web sin cambiar ViewModels ni pantallas.
// Por ahora solo tiene las funciones de servicios; el resto se agrega en los commits 2 al 5
interface VetRepository {

    // ---------- Servicios (ejemplo resuelto) ----------
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
}