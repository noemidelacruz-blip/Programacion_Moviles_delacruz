package com.delacruz.huellitasvet.data.repository

import androidx.room.withTransaction
import com.delacruz.huellitasvet.data.local.VetDatabase
import com.delacruz.huellitasvet.data.local.entity.Cita
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.local.relation.MascotaConDueno
import com.delacruz.huellitasvet.model.ConteoServicio
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.Flow

// Implementacion del repositorio con Room: cada funcion delega en el DAO correspondiente.
// Las funciones observar* no son suspend porque devuelven Flow
class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    private val usuarioDao = db.usuarioDao()
    private val servicioDao = db.servicioDao()
    private val mascotaDao = db.mascotaDao()
    private val citaDao = db.citaDao()

    // ---------- Usuarios ----------
    // Devuelve el usuario si el correo y la clave coinciden, o null si no
    override suspend fun login(correo: String, clave: String): Usuario? =
        usuarioDao.login(correo, clave)
    // Recupera un usuario por id (restaurar la sesion guardada)
    override suspend fun obtenerUsuario(id: Int): Usuario? = usuarioDao.obtener(id)

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

    override suspend fun servicioTieneCitas(id: Int): Boolean = servicioDao.contarCitas(id) > 0

    // ---------- Mascotas ----------
    override fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>> =
        mascotaDao.observarPorDueno(duenoId)

    override fun observarMascota(id: Int): Flow<Mascota?> = mascotaDao.observarUna(id)

    override fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>> = mascotaDao.observarTodas()

    // Si la mascota no tiene id (0) es nueva y se inserta; si ya tiene id, se actualiza
    override suspend fun guardarMascota(m: Mascota) {
        if (m.id == 0) mascotaDao.insertar(m) else mascotaDao.actualizar(m)
    }

    override suspend fun eliminarMascota(m: Mascota) = mascotaDao.eliminar(m)

    override suspend fun mascotaTieneCitas(id: Int): Boolean = mascotaDao.contarCitas(id) > 0

    // ---------- Citas ----------
    override fun observarHorasOcupadas(fecha: String): Flow<List<String>> =
        citaDao.observarHorasOcupadas(fecha)

    // Copia el precio del servicio (foto del momento), arma la cita PENDIENTE y la inserta.
    // La comprobacion del horario y el insert van en una transaccion para que dos citas
    // no puedan quedar con la misma fecha y hora. Devuelve -1 si el horario ya estaba ocupado
    override suspend fun crearCita(
        mascotaId: Int,
        servicioId: Int,
        fecha: String,
        hora: String,
        nota: String
    ): Long = db.withTransaction {
        if (citaDao.contarOcupadas(fecha, hora) > 0) {
            -1L
        } else {
            val servicio = requireNotNull(servicioDao.obtener(servicioId)) {
                "El servicio $servicioId no existe"
            }
            citaDao.insertar(
                Cita(
                    mascotaId = mascotaId,
                    servicioId = servicioId,
                    fecha = fecha,
                    hora = hora,
                    nota = nota,
                    estado = EstadoCita.PENDIENTE.name,
                    precio = servicio.precio,
                    fechaCreacion = System.currentTimeMillis()
                )
            )
        }
    }

    override fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>> =
        citaDao.observarPorDueno(duenoId)

    override fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>> =
        citaDao.observarPorFecha(fecha)

    override fun observarCita(id: Int): Flow<CitaConDetalle?> = citaDao.observarUna(id)

    override fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>> =
        citaDao.observarHistorial(mascotaId)

    // Guarda el nombre del estado (PENDIENTE, CONFIRMADA, ...) en la tabla
    override suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita) =
        citaDao.actualizarEstado(id, estado.name)

    // Todo en UNA transaccion: guarda la atencion en la cita y actualiza el peso de la mascota.
    // Si algo falla, no queda nada a medias
    override suspend fun registrarAtencion(
        citaId: Int,
        diagnostico: String,
        tratamiento: String,
        peso: Double
    ) {
        db.withTransaction {
            val cita = citaDao.obtener(citaId) ?: return@withTransaction
            citaDao.registrarAtencion(citaId, diagnostico, tratamiento, peso)
            mascotaDao.actualizarPeso(cita.mascotaId, peso)
        }
    }

    // ---------- Panel ----------
    override fun observarCitasDeFecha(fecha: String): Flow<Int> = citaDao.contarPorFecha(fecha)

    override fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int> =
        citaDao.contarPorEstado(estado.name)

    override fun observarCitasPorServicio(): Flow<List<ConteoServicio>> =
        citaDao.contarPorServicio()

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> =
        citaDao.ingresosDesde(fechaInicio)
}