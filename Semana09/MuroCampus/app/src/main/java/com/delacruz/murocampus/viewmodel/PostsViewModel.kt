package com.delacruz.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.murocampus.data.PostRepository
import com.delacruz.murocampus.model.Comentario
import com.delacruz.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Datos que necesita la pantalla de detalle: la publicación y sus comentarios. */
data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

/**
 * ViewModel del muro. Solo habla con la interface PostRepository y expone
 * estados inmutables para la pantalla.
 */
class PostsViewModel(
    private val repo: PostRepository
) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    init {
        cargar()
    }

    /** Pone Cargando, pide la lista y la convierte en Exito o Error. */
    fun cargar() {
        filtrar(_usuarioFiltro.value)
    }

    /**
     * Guarda el filtro elegido y recarga: null significa Todos.
     * Con un usuario usa porUsuario(userId), es decir GET posts?userId=N.
     */
    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = if (userId == null) {
                repo.listar()
            } else {
                repo.porUsuario(userId)
            }
            resultado
                .onSuccess { posts -> _lista.value = UiState.Exito(posts) }
                .onFailure { error -> _lista.value = UiState.Error(mensajeDe(error)) }
        }
    }

    /**
     * Pide la publicación y sus comentarios a la vez con async. Si una de las dos
     * llamadas falla, todo el detalle muestra el error.
     */
    fun abrir(id: Int) {
        viewModelScope.launch {
            _detalle.value = UiState.Cargando

            val resultado = coroutineScope {
                val post = async { repo.detalle(id) }
                val comentarios = async { repo.comentarios(id) }

                val postResultado = post.await()
                val comentariosResultado = comentarios.await()

                when {
                    postResultado.isSuccess && comentariosResultado.isSuccess ->
                        Result.success(
                            DetalleUi(
                                post = postResultado.getOrThrow(),
                                comentarios = comentariosResultado.getOrThrow()
                            )
                        )

                    postResultado.isFailure ->
                        Result.failure(postResultado.exceptionOrNull()!!)

                    else ->
                        Result.failure(comentariosResultado.exceptionOrNull()!!)
                }
            }

            resultado
                .onSuccess { detalle -> _detalle.value = UiState.Exito(detalle) }
                .onFailure { error -> _detalle.value = UiState.Error(mensajeDe(error)) }
        }
    }
}
