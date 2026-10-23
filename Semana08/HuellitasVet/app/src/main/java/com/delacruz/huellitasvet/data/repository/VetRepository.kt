package com.delacruz.huellitasvet.data.repository

import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.data.local.entity.Vacuna
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.local.relation.MascotaConDueno
import com.delacruz.huellitasvet.model.ConteoFecha
import com.delacruz.huellitasvet.model.ConteoServicio
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.Flow
import com.delacruz.huellitasvet.model.UltimaVisita
import com.delacruz.huellitasvet.model.Rol
// El contrato entre los ViewModels y los datos. Es una interface para que mas adelante
// se pueda conectar a un servicio web sin cambiar ViewModels ni pantallas.
// Regla de oro: lectura que debe actualizar la pantalla -> Flow (sin suspend);
// escritura o accion unica -> suspend
interface VetRepository {

    // ---------- Usuarios ----------
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun obtenerUsuario(id: Int): Usuario?
    suspend fun obtenerUsuarioPorRol(rol: Rol): Usuario?

    // ---------- Servicios (ejemplo resuelto) ----------
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneCitas(id: Int): Boolean
    suspend fun obtenerServicio(id: Int): Servicio?

    // ---------- Mascotas ----------
    fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>>
    fun observarMascota(id: Int): Flow<Mascota?>
    fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>>
    suspend fun guardarMascota(m: Mascota)
    suspend fun eliminarMascota(m: Mascota)
    suspend fun mascotaTieneCitas(id: Int): Boolean

    // ---------- Vacunas ----------
    fun observarVacunas(mascotaId: Int): Flow<List<Vacuna>>
    suspend fun guardarVacuna(v: Vacuna)
    suspend fun eliminarVacuna(v: Vacuna)

    // ---------- Citas ----------
    fun observarHorasOcupadas(fecha: String): Flow<List<String>>

    // Devuelve el id de la cita creada, o -1 si el horario ya estaba ocupado
    suspend fun crearCita(
        mascotaId: Int,
        servicioId: Int,
        fecha: String,
        hora: String,
        nota: String
    ): Long

    fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>>
    fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>>
    fun observarCita(id: Int): Flow<CitaConDetalle?>
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>
    suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita)
    suspend fun registrarAtencion(
        citaId: Int,
        diagnostico: String,
        tratamiento: String,
        peso: Double,
        fechaControl: String?
    )

    // ---------- Panel del veterinario ----------
    fun observarCitasDeFecha(fecha: String): Flow<Int>
    fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int>
    fun observarCitasPorServicio(): Flow<List<ConteoServicio>>

    // Citas no canceladas de cada dia entre inicio y fin (calendario mensual de la Agenda)
    fun observarCitasPorDia(inicio: String, fin: String): Flow<List<ConteoFecha>>
    fun observarIngresosDesde(fechaInicio: String): Flow<Double>
    fun observarAtencionesDesde(fechaInicio: String): Flow<Int>
    fun observarUltimasVisitas(): Flow<List<UltimaVisita>>
}