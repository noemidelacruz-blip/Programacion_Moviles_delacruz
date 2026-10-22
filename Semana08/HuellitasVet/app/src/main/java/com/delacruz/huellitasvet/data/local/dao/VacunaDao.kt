package com.delacruz.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.delacruz.huellitasvet.data.local.entity.Vacuna
import kotlinx.coroutines.flow.Flow

@Dao
interface VacunaDao {

    @Insert
    suspend fun insertar(v: Vacuna): Long

    @Insert
    suspend fun insertarTodos(lista: List<Vacuna>)

    @Update
    suspend fun actualizar(v: Vacuna)

    @Delete
    suspend fun eliminar(v: Vacuna)

    @Query("SELECT * FROM vacunas WHERE mascotaId = :mascotaId ORDER BY fecha DESC")
    fun observarDeMascota(mascotaId: Int): Flow<List<Vacuna>>
}
