package com.delacruz.catalogo.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Lo que responde la API (DummyJSON) para un producto.
 *
 * Es un modelo SOLO de la capa de red: no se usa en la interfaz.
 * Los nombres en español del proyecto se mapean con @SerializedName a las
 * claves reales del JSON de dummyjson.com.
 */
data class ItemDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("description") val descripcion: String,
    @SerializedName("brand") val marca: String?,
    @SerializedName("price") val precio: Double,
    @SerializedName("rating") val calificacion: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("category") val categoria: String,
    @SerializedName("thumbnail") val imagenUrl: String?
)
