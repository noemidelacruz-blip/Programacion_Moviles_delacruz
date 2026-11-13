package com.delacruz.catalogo.data.repository

import com.delacruz.catalogo.data.local.ItemDao
import com.delacruz.catalogo.data.mapper.aEntity
import com.delacruz.catalogo.data.mapper.aModelo
import com.delacruz.catalogo.data.remote.ApiService
import com.delacruz.catalogo.domain.model.Item
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementación del repositorio (hito H9).
 *
 * Regla de arquitectura: Room es la fuente única de verdad.
 * - items y favoritos salen SIEMPRE de Room.
 * - Retrofit solo actualiza Room cuando se llama a refrescar().
 *
 * Así la app funciona sin internet y los favoritos sobreviven a la
 * sincronización.
 */
//ItemRepositoryImpl realiza las llamadas para obtener los productos.
class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> =
        dao.todos().map { lista -> lista.map { it.aModelo() } }

    override val favoritos: Flow<List<Item>> =
        dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    /**
     * Descarga la lista, la guarda en Room y devuelve cuántos guardó.
     *
     * Antes de guardar lee los ids favoritos y los vuelve a aplicar, porque
     * el @Upsert sobrescribe esFavorito y si no se hace, los favoritos
     * desaparecen al sincronizar.
     */
    override suspend fun refrescar(): Result<Int> = try {
        val idsFavoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar().products.map { dto ->
            dto.aEntity(esFavorito = dto.id in idsFavoritos)
        }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {
        // Sin internet o timeout: se conserva lo que ya está en Room.
        Result.failure(e)
    } catch (e: HttpException) {
        // 404, 500, etc.
        Result.failure(e)
    } catch (e: CancellationException) {
        // Si se cancela la tarea, dejamos que la cancelación continúe.
        throw e
    } catch (e: Exception) {
        // Si se cancela la tarea, dejamos que la cancelación continúe.
        Result.failure(e)
    }

    /**
     * Detalle desde el segundo endpoint.
     * Si no hay internet, intenta devolver el producto guardado en Room.
     */
    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        Result.success(api.obtenerDetalle(id).aModelo())
    } catch (e: IOException) {
        val guardado = dao.porId(id)
        if (guardado != null) Result.success(guardado.aModelo()) else Result.failure(e)
    } catch (e: HttpException) {
        val guardado = dao.porId(id)
        if (guardado != null) Result.success(guardado.aModelo()) else Result.failure(e)
    } catch (e: CancellationException) {
        // Si se cancela la tarea, dejamos que la cancelación continúe.
        throw e
    } catch (e: Exception) {
        // Si ocurre otro error, devolvemos un resultado fallido.
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        dao.alternarFavorito(id)
    }

    override suspend fun cantidadGuardada(): Int = dao.contar()

    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()

    override suspend fun limpiarCache() {
        dao.borrarTodo()
    }
}
