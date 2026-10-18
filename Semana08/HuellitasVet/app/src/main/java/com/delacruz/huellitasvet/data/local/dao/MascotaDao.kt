package com.delacruz.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.relation.MascotaConDueno
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {

    // ---------- Escritura (suspend) ----------
    @Insert
    suspend fun insertar(m: Mascota): Long

    @Update
    suspend fun actualizar(m: Mascota)

    @Delete
    suspend fun eliminar(m: Mascota)

    // Actualiza solo el peso (lo usa registrar la atencion de una cita)
    @Query("UPDATE mascotas SET peso = :peso WHERE id = :id")
    suspend fun actualizarPeso(id: Int, peso: Double)

    // ---------- Lectura (Flow) ----------
    // Mascotas de un dueno (el dueno solo ve las suyas)
    @Query("SELECT * FROM mascotas WHERE duenoId = :duenoId ORDER BY nombre")
    fun observarPorDueno(duenoId: Int): Flow<List<Mascota>>

    // Una mascota por id (ficha y formulario de edicion)
    @Query("SELECT * FROM mascotas WHERE id = :id")
    fun observarUna(id: Int): Flow<Mascota?>

    // Todas las mascotas con su dueno (pantalla Pacientes del veterinario)
    @Transaction
    @Query("SELECT * FROM mascotas ORDER BY nombre")
    fun observarTodas(): Flow<List<MascotaConDueno>>

    // ---------- Reglas ----------
    // Cuantas citas tiene una mascota (una mascota con citas no se puede eliminar)
    @Query("SELECT COUNT(*) FROM citas WHERE mascotaId = :mascotaId")
    suspend fun contarCitas(mascotaId: Int): Int
}