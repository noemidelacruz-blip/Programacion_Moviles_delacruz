package com.delacruz.catalogo.data.remote

import com.delacruz.catalogo.data.remote.dto.ItemDto
import com.delacruz.catalogo.data.remote.dto.ProductosResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Endpoints de DummyJSON para el tema Productos.
 *
 * Endpoint 1 (lista):   GET /products/category/{slug}
 * Endpoint 2 (detalle): GET /products/{id}
 *
 * La variante asignada es la categoría "beauty".
 */
interface ApiService {

    @GET("products/category/{slug}")
    suspend fun listar(@Path("slug") slug: String = CATEGORIA_BEAUTY): ProductosResponseDto

    @GET("products/{id}")
    suspend fun obtenerDetalle(@Path("id") id: Int): ItemDto

    companion object {
        /** Variante del tema Productos asignada a este proyecto. */
        const val CATEGORIA_BEAUTY = "beauty"
    }
}
