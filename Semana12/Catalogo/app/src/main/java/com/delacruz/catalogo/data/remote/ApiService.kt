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
//define los endpoinst
interface ApiService {

    // aqui esta filtrando  por categoria el parametro  (@Path("slug") con el valor beauty.
    @GET("products/category/{slug}") //Obtiene la lista de productos de la categoría beauty.
    suspend fun listar(@Path("slug") slug: String = CATEGORIA_BEAUTY): ProductosResponseDto

    @GET("products/{id}") //Obtiene la información detallada de un producto específico mediante su ID.
    suspend fun obtenerDetalle(@Path("id") id: Int): ItemDto

    companion object {
        /** Productos asignada a este proyecto. */
        const val CATEGORIA_BEAUTY = "beauty"
    }
}
