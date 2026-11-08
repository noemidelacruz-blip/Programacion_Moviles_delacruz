package com.delacruz.catalogo.data.repository

import com.delacruz.catalogo.data.mapper.aModelo
import com.delacruz.catalogo.data.remote.ApiService
import com.delacruz.catalogo.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import retrofit2.HttpException
import java.io.IOException

/**
 * Implementación del repositorio (hito H6).
 *
 * Por ahora guarda la lista en memoria; en el hito H9 se reemplaza por Room,
 * que será la fuente única de verdad. El ViewModel no nota el cambio porque
 * depende de la interface ItemRepository.
 */
class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    private val _items = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = _items.asStateFlow()

    override val favoritos: Flow<List<Item>> =
        _items.map { lista -> lista.filter { it.esFavorito } }

    /**
     * Llama al endpoint de la lista y reemplaza lo que había en memoria.
     * Devuelve cuántos productos se guardaron.
     */
    override suspend fun refrescar(): Result<Int> = try {
        val respuesta = api.listar()
        val nuevos = respuesta.products.map { it.aModelo() }
        _items.value = nuevos
        Result.success(nuevos.size)
    } catch (e: IOException) {
        // Sin internet o timeout
        Result.failure(e)
    } catch (e: HttpException) {
        // 404, 500, etc.
        Result.failure(e)
    }

    /** Llama al endpoint de detalle y convierte el DTO en modelo de la UI. */
    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        Result.success(api.obtenerDetalle(id).aModelo())
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        _items.update { lista ->
            lista.map { item ->
                if (item.id == id) item.copy(esFavorito = !item.esFavorito) else item
            }
        }
    }

    override suspend fun cantidadGuardada(): Int = _items.value.size

    override suspend fun cantidadFavoritos(): Int = _items.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        _items.value = emptyList()
    }
}
