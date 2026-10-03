package com.delacruz.tecsupstore.model

/**
 * Modelo de datos que representa un producto del catálogo de TECSUP Store.
 *
 * @property id Identificador único del producto.
 * @property nombre Nombre descriptivo del producto.
 * @property precio Precio del producto.
 */
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: String
)