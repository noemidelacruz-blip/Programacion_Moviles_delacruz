package com.delacruz.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.delacruz.huellitasvet.data.local.entity.Cita
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.model.ConteoServicio
import kotlinx.coroutines.flow.Flow

import com.delacruz.huellitasvet.model.UltimaVisita

// Los estados se comparan con el texto del enum EstadoCita ('CANCELADA', 'ATENDIDA')
@Dao
interface CitaDao {

    // ---------- Escritura (suspend) ----------
    @Insert
    suspend fun insertar(c: Cita): Long

    @Query("UPDATE citas SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)

    // Pasa la cita a ATENDIDA y guarda diagnostico, tratamiento y peso registrado
    @Query(
        "UPDATE citas SET estado = 'ATENDIDA', diagnostico = :diagnostico, " +
                "tratamiento = :tratamiento, pesoRegistrado = :peso WHERE id = :id"
    )
    suspend fun registrarAtencion(id: Int, diagnostico: String, tratamiento: String, peso: Double)

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun obtener(id: Int): Cita?

    // ---------- Horarios (una cita por horario) ----------
    // Cuantas citas NO canceladas hay en esa fecha y hora (se usa para validar antes de crear)
    @Query("SELECT COUNT(*) FROM citas WHERE fecha = :fecha AND hora = :hora AND estado != 'CANCELADA'")
    suspend fun contarOcupadas(fecha: String, hora: String): Int

    // Horas ocupadas de un dia: los chips de hora las muestran grises y tachadas
    @Query("SELECT hora FROM citas WHERE fecha = :fecha AND estado != 'CANCELADA'")
    fun observarHorasOcupadas(fecha: String): Flow<List<String>>

    // ---------- Lectura con detalle (Flow + @Transaction) ----------
    // Citas de un dueno: INNER JOIN con mascotas para filtrar por el dueno de la mascota
    @Transaction
    @Query(
        "SELECT c.* FROM citas c INNER JOIN mascotas m ON c.mascotaId = m.id " +
                "WHERE m.duenoId = :duenoId ORDER BY c.fecha, c.hora"
    )
    fun observarPorDueno(duenoId: Int): Flow<List<CitaConDetalle>>

    // Agenda de un dia, ordenada por hora
    @Transaction
    @Query("SELECT * FROM citas WHERE fecha = :fecha ORDER BY hora")
    fun observarPorFecha(fecha: String): Flow<List<CitaConDetalle>>

    // Una cita con su detalle (pantalla Atender cita)
    @Transaction
    @Query("SELECT * FROM citas WHERE id = :id")
    fun observarUna(id: Int): Flow<CitaConDetalle?>

    // Historial clinico: solo citas ATENDIDAS de la mascota, la mas reciente primero
    @Transaction
    @Query(
        "SELECT * FROM citas WHERE mascotaId = :mascotaId AND estado = 'ATENDIDA' " +
                "ORDER BY fecha DESC, hora DESC"
    )
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>

    // ---------- Panel del veterinario ----------
    // Citas de una fecha sin contar las canceladas
    @Query("SELECT COUNT(*) FROM citas WHERE fecha = :fecha AND estado != 'CANCELADA'")
    fun contarPorFecha(fecha: String): Flow<Int>

    // Cuantas citas hay en un estado (pendientes, atendidas, etc.)
    @Query("SELECT COUNT(*) FROM citas WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    // Citas por servicio: JOIN con servicios y GROUP BY. Los alias coinciden con ConteoServicio
    @Query(
        "SELECT s.nombre AS nombre, COUNT(c.id) AS total FROM citas c " +
                "INNER JOIN servicios s ON c.servicioId = s.id GROUP BY s.id ORDER BY total DESC"
    )
    fun contarPorServicio(): Flow<List<ConteoServicio>>

    // Ingresos: suma del precio de las citas ATENDIDAS desde una fecha (0.0 si no hay ninguna)
    @Query(
        "SELECT COALESCE(SUM(precio), 0.0) FROM citas " +
                "WHERE estado = 'ATENDIDA' AND fecha >= :fechaInicio"
    )
    fun ingresosDesde(fechaInicio: String): Flow<Double>


    // Cuantas citas ATENDIDAS hay desde una fecha (las "atenciones del mes" del panel)
    @Query("SELECT COUNT(*) FROM citas WHERE estado = 'ATENDIDA' AND fecha >= :fechaInicio")
    fun contarAtendidasDesde(fechaInicio: String): Flow<Int>

    // Ultima atencion de cada mascota: la fecha mas reciente de sus citas ATENDIDAS (para Pacientes)
    @Query(
        "SELECT mascotaId AS mascotaId, MAX(fecha) AS fecha FROM citas " +
                "WHERE estado = 'ATENDIDA' GROUP BY mascotaId"
    )
    fun ultimasVisitas(): Flow<List<UltimaVisita>>
}
