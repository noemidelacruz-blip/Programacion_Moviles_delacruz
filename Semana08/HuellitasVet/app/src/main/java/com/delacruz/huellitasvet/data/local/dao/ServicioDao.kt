package com.delacruz.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.delacruz.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

// Regla de oro: lecturas que deben actualizar la pantalla -> Flow (sin suspend);
// escrituras o acciones unicas -> suspend
@Dao
interface ServicioDao {

    // Todos los servicios (pantalla Servicios del veterinario)
    @Query("SELECT * FROM servicios ORDER BY nombre")
    fun observarTodos(): Flow<List<Servicio>>

    // Solo los activos (pantalla Pedir cita del dueno)
    @Query("SELECT * FROM servicios WHERE activo = 1 ORDER BY nombre")
    fun observarActivos(): Flow<List<Servicio>>

    // Un servicio por id (para editar y para copiar el precio al crear una cita)
    @Query("SELECT * FROM servicios WHERE id = :id")
    suspend fun obtener(id: Int): Servicio?

    // Cuantos servicios hay (se usa para sembrar los datos iniciales solo una vez)
    @Query("SELECT COUNT(*) FROM servicios")
    suspend fun contar(): Int

    // Inserta o reemplaza (sirve para crear y para guardar cambios del formulario)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(s: Servicio): Long

    @Insert
    suspend fun insertarTodos(lista: List<Servicio>)

    @Update
    suspend fun actualizar(s: Servicio)

    @Delete
    suspend fun eliminar(s: Servicio)

    // Activa o desactiva un servicio (el switch de la lista)
    @Query("UPDATE servicios SET activo = :activo WHERE id = :id")
    suspend fun cambiarActivo(id: Int, activo: Boolean)
}