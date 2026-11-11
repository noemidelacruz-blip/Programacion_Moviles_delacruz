package com.delacruz.catalogo.data.repository

import com.delacruz.catalogo.domain.model.Item
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de productos.
 *
 * Regla de la arquitectura: Room es la fuente única de verdad, por eso la lista
 * y los favoritos salen como Flow desde la base de datos. Retrofit solo actualiza
 * Room cuando se llama a refrescar().
 *
 * La implementación real (ItemRepositoryImpl) llega en el hito H6.
 */
interface ItemRepository {

    /** Lista de productos guardados en Room. */
    val items: Flow<List<Item>>

    /** Lista de productos marcados como favoritos. */
    val favoritos: Flow<List<Item>>

    /** Descarga la lista desde la API, la guarda en Room y devuelve cuántos guardó. */
    suspend fun refrescar(): Result<Int>

    /** Obtiene el detalle de un producto desde el segundo endpoint de la API. */
    suspend fun obtenerDetalle(id: Int): Result<Item>

    /** Marca o desmarca un producto como favorito. */
    suspend fun alternarFavorito(id: Int)

    /** Cantidad de productos guardados en Room (para el panel Admin). */
    suspend fun cantidadGuardada(): Int

    /** Cantidad de productos favoritos (para el panel Admin). */
    suspend fun cantidadFavoritos(): Int

    /** Borra la caché local (para el panel Admin). */
    suspend fun limpiarCache()
}
