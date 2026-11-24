package com.delacruz.murocampus.data.remote

import com.delacruz.murocampus.model.Comentario
import com.delacruz.murocampus.model.NuevoPost
import com.delacruz.murocampus.model.Post
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface de Retrofit con las llamadas del servicio.
 * Las rutas no empiezan con "/" para no reemplazar el camino de la URL base.
 */
interface PostApi {

    /** GET posts: lista completa de publicaciones. */
    @GET("posts")
    suspend fun listar(): List<Post>

    /** GET posts/{id}: una publicación concreta. */
    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    /** GET posts/{id}/comments: comentarios de una publicación. */
    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    /** GET posts?userId=2: publicaciones filtradas por usuario. */
    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    /** POST posts: crea una publicación y devuelve la respuesta del servidor. */
    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post
}
