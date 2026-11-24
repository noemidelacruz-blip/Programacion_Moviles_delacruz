package com.delacruz.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.murocampus.data.PostRepository
import com.delacruz.murocampus.model.Comentario
import com.delacruz.murocampus.model.NuevoPost
import com.delacruz.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    /** Texto que escribe el usuario en el buscador del muro. */
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    /**
     * Lista que ve la pantalla: lo descargado por filtrar() pasado por el texto
     * del buscador. Se recalcula sola cuando cambia cualquiera de los dos, así
     * la vista no tiene que filtrar nada.
     */
    val listaVisible: StateFlow<UiState<List<Post>>> =
        combine(_lista, _busqueda) { estado, texto -> estado.conBusqueda(texto) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, _lista.value)

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    /** null significa que todavía no se ha enviado ninguna publicación. */
    private val _creacion = MutableStateFlow<UiState<Post>?>(null)
    val creacion: StateFlow<UiState<Post>?> = _creacion.asStateFlow()

    /** null significa que todavía no se intentó borrar ninguna publicación. */
    private val _eliminacion = MutableStateFlow<UiState<Unit>?>(null)
    val eliminacion: StateFlow<UiState<Unit>?> = _eliminacion.asStateFlow()

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

    /** Guarda el texto del buscador. El filtrado ocurre en listaVisible. */
    fun buscar(texto: String) {
        _busqueda.value = texto
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

    /**
     * Envía una publicación nueva con POST. El servidor de práctica no la guarda,
     * pero responde con la publicación creada y un id nuevo.
     */
    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _creacion.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { creado -> _creacion.value = UiState.Exito(creado) }
                .onFailure { error -> _creacion.value = UiState.Error(mensajeDe(error)) }
        }
    }

    /** Vuelve la pantalla de crear a su estado inicial (aún no se envió nada). */
    fun limpiarCreacion() {
        _creacion.value = null
    }

    /**
     * Borra una publicación con DELETE. El servidor de práctica responde 200
     * pero no guarda el borrado, así que además se quita de la lista en memoria
     * para que el muro lo refleje hasta que se recargue.
     */
    fun eliminar(id: Int) {
        viewModelScope.launch {
            _eliminacion.value = UiState.Cargando
            repo.eliminar(id)
                .onSuccess {
                    val actual = _lista.value
                    if (actual is UiState.Exito) {
                        _lista.value = UiState.Exito(
                            actual.datos.filterNot { post -> post.id == id }
                        )
                    }
                    _eliminacion.value = UiState.Exito(Unit)
                }
                .onFailure { error -> _eliminacion.value = UiState.Error(mensajeDe(error)) }
        }
    }

    /** Deja la eliminación en su estado inicial al salir del detalle. */
    fun limpiarEliminacion() {
        _eliminacion.value = null
    }
}

/**
 * Deja solo las publicaciones cuyo título contiene el texto, sin distinguir
 * mayúsculas. Se aplica sobre lo que ya está descargado: no vuelve a llamar a
 * la API para buscar.
 */
private fun UiState<List<Post>>.conBusqueda(texto: String): UiState<List<Post>> {
    if (this !is UiState.Exito) return this
    val limpio = texto.trim()
    if (limpio.isEmpty()) return this
    return UiState.Exito(
        datos.filter { post -> post.title.contains(limpio, ignoreCase = true) }
    )
}
