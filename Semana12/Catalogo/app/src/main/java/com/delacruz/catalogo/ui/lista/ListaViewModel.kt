package com.delacruz.catalogo.ui.lista

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
 * ViewModel de la lista (hito H6).
 *
 * Ya consume la API a través de la interface ItemRepository: el ViewModel no
 * sabe si los datos vienen de Retrofit o de Room.
 *
 * Regla: la pantalla solo puede leer [estado]; nadie más lo modifica.
 */
class ListaViewModel(
    private val repo: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    /** Evita mostrar "vacío" antes de que termine la primera descarga. */
    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            repo.items.collect { lista ->
                if (lista.isNotEmpty() || refrescoTerminado) {
                    _estado.value = UiState.Exito(lista)
                }
            }
        }
        refrescar()
    }

    /** Descarga la lista desde la API (también es el botón Reintentar). */
    fun refrescar() {
        viewModelScope.launch {
            val resultado = repo.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess {
                    if (_estado.value is UiState.Cargando) {
                        _estado.value = UiState.Exito(emptyList())
                    }
                }
                .onFailure { error ->
                    // Si no hay nada guardado, se muestra el error con Reintentar.
                    if (repo.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(mensajeDe(error))
                    }
                }
        }
    }

    private fun mensajeDe(error: Throwable): String = when (error) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${error.code()}"
        else -> "Ocurrió un error inesperado"
    }
}

/**
 * Factory que recibe el AppContainer y construye el ViewModel.
 * Así el ViewModel no crea el repositorio por su cuenta.
 */
class ListaViewModelFactory(
    private val repo: ItemRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaViewModel::class.java)) {
            return ListaViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
