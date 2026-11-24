package com.delacruz.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.murocampus.data.PostRepository
import com.delacruz.murocampus.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel del muro. Solo habla con la interface PostRepository y expone
 * estados inmutables para la pantalla.
 */
class PostsViewModel(
    private val repo: PostRepository
) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    init {
        cargar()
    }

    /** Pone Cargando, pide la lista y la convierte en Exito o Error. */
    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            repo.listar()
                .onSuccess { posts -> _lista.value = UiState.Exito(posts) }
                .onFailure { error -> _lista.value = UiState.Error(mensajeDe(error)) }
        }
    }
}
