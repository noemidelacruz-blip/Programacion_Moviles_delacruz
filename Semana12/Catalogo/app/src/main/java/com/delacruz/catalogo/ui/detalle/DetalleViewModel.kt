package com.delacruz.catalogo.ui.detalle

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
import retrofit2.HttpException
import java.io.IOException

/**
 * ViewModel de la pantalla de detalle (hito H7).
 *
 * Usa el segundo endpoint de la API a través del repositorio.
 * La pantalla solo lee [estado] y llama a [cargar].
 */
class DetalleViewModel(
    private val repo: ItemRepository,
    private val id: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargar()
    }

    /** Pide el detalle del producto (también es el botón Reintentar). */
    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.obtenerDetalle(id)
                .onSuccess { item -> _estado.value = UiState.Exito(item) }
                .onFailure { error -> _estado.value = UiState.Error(mensajeDe(error)) }
        }
    }

    private fun mensajeDe(error: Throwable): String = when (error) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${error.code()}"
        else -> "Ocurrió un error inesperado"
    }
}

/** Factory que recibe el repositorio y el id del producto seleccionado. */
class DetalleViewModelFactory(
    private val repo: ItemRepository,
    private val id: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleViewModel::class.java)) {
            return DetalleViewModel(repo, id) as T
        }
        throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
