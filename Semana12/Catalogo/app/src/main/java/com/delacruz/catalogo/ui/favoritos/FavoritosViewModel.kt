package com.delacruz.catalogo.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.delacruz.catalogo.data.repository.ItemRepository
import com.delacruz.catalogo.domain.model.Item
import com.delacruz.catalogo.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de favoritos (hito H10).
 *
 * La lista sale de Room (repo.favoritos), así que se actualiza sola cuando se
 * marca o desmarca un producto y sobrevive al cierre de la app.
 */
class FavoritosViewModel(
    private val repo: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repo.favoritos.collect { lista ->
                _estado.value = UiState.Exito(lista)
            }
        }
    }

    /** Marca o desmarca un favorito (al desmarcarlo desaparece de esta lista). */
    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repo.alternarFavorito(id)
        }
    }
}

/** Factory para crear el ViewModel con el repositorio. */
class FavoritosViewModelFactory(
    private val repo: ItemRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritosViewModel::class.java)) {
            return FavoritosViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
