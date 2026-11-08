package com.delacruz.catalogo.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a la tabla "items" (hito H8).
 *
 * Los métodos que devuelven Flow avisan solos a la interfaz cuando la tabla
 * cambia, por eso Room es la fuente única de verdad.
 */
@Dao
interface ItemDao {

    /** Todos los productos ordenados por título. */
    @Query("SELECT * FROM items ORDER BY titulo")
    fun todos(): Flow<List<ItemEntity>>

    /** Solo los productos marcados como favoritos. */
    @Query("SELECT * FROM items WHERE esFavorito = 1 ORDER BY titulo")
    fun favoritos(): Flow<List<ItemEntity>>

    /** Ids de los favoritos: se usan para no perderlos al sincronizar. */
    @Query("SELECT id FROM items WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<Int>

    /** Cantidad de productos guardados (panel Admin). */
    @Query("SELECT COUNT(*) FROM items")
    suspend fun contar(): Int

    /** Cantidad de favoritos (panel Admin). */
    @Query("SELECT COUNT(*) FROM items WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    /** Un producto por id, para el detalle sin internet. */
    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    suspend fun porId(id: Int): ItemEntity?

    /** Inserta o actualiza la lista descargada. */
    @Upsert
    suspend fun guardarTodos(items: List<ItemEntity>)

    /** Cambia el estado de favorito de un producto. */
    @Query("UPDATE items SET esFavorito = NOT esFavorito WHERE id = :id")
    suspend fun alternarFavorito(id: Int)

    /** Borra toda la caché (panel Admin). */
    @Query("DELETE FROM items")
    suspend fun borrarTodo()
}
