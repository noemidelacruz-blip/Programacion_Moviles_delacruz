package com.delacruz.huellitasvet.data.repository

import com.delacruz.huellitasvet.data.local.VetDatabase
import com.delacruz.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

// Implementacion del repositorio con Room: cada funcion delega en el DAO correspondiente.
// Las funciones observar* no son suspend porque devuelven Flow
class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    private val servicioDao = db.servicioDao()

    // ---------- Servicios ----------
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()

    // insertar con REPLACE sirve para crear un servicio nuevo y para guardar cambios
    override suspend fun guardarServicio(s: Servicio) {
        servicioDao.insertar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) = servicioDao.eliminar(s)

    override suspend fun cambiarActivo(id: Int, activo: Boolean) =
        servicioDao.cambiarActivo(id, activo)
}