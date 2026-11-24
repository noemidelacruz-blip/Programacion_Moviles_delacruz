package com.delacruz.murocampus.data

import com.delacruz.murocampus.data.remote.PostApi
import com.delacruz.murocampus.model.Comentario
import com.delacruz.murocampus.model.NuevoPost
import com.delacruz.murocampus.model.Post
import kotlinx.coroutines.CancellationException

/**
 * Contrato de acceso a las publicaciones. El ViewModel solo conoce esta interface
 * y siempre recibe un Result: nunca una excepción.
 */
interface PostRepository {

    suspend fun listar(): Result<List<Post>>

    suspend fun detalle(id: Int): Result<Post>

    suspend fun comentarios(id: Int): Result<List<Comentario>>

    suspend fun porUsuario(userId: Int): Result<List<Post>>

    suspend fun crear(nuevo: NuevoPost): Result<Post>
}

/**
 * Implementación que consume PostApi y convierte cada fallo de red en Result.failure.
 * La cancelación de la corrutina se relanza para no tragarla.
 */
class PostRepositoryRemote(
    private val api: PostApi
) : PostRepository {

    override suspend fun listar(): Result<List<Post>> = llamar { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> =
        llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> =
        llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e // nunca se traga la cancelación
        } catch (e: Exception) {
            Result.failure(e)
        }
}
