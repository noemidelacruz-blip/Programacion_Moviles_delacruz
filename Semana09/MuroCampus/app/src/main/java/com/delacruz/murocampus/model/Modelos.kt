package com.delacruz.murocampus.model

import kotlinx.serialization.Serializable

/**
 * Ejemplo entregado [E]: usuario del servicio.
 * Sirve de referencia para comparar cómo se modela el JSON.
 */
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

/**
 * Publicación del muro. Los nombres coinciden exactamente con los del JSON
 * de JSONPlaceholder: userId, id, title y body.
 */
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

/**
 * Comentario de una publicación: postId, id, name, email y body.
 */
@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

/**
 * Datos que enviamos al crear una publicación. No lleva id porque lo asigna
 * el servidor en la respuesta.
 */
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)
