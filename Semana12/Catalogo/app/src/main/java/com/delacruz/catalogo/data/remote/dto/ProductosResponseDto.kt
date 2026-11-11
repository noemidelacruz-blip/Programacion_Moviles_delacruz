package com.delacruz.catalogo.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Envoltorio que devuelve DummyJSON en el endpoint de la lista:
 * { "products": [ ... ], "total": 5, "skip": 0, "limit": 30 }
 *
 * Por eso el repositorio debe usar .products y no la respuesta completa.
 */
data class ProductosResponseDto(
    @SerializedName("products") val products: List<ItemDto>,
    @SerializedName("total") val total: Int = 0,
    @SerializedName("skip") val skip: Int = 0,
    @SerializedName("limit") val limit: Int = 0
)
