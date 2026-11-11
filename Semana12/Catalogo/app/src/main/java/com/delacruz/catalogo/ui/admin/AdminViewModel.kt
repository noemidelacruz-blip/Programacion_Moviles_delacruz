package com.delacruz.catalogo.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.delacruz.catalogo.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado del panel Admin (hito H14).
 *
 * Solo el rol admin llega a esta pantalla. Aquí se muestran los totales de la
 * caché local y los botones Sincronizar y Limpiar caché.
 */
data class AdminUiState(
    val cargando: Boolean = true,
    val totalGuardados: Int = 0,
    val totalFavoritos: Int = 0,
    val trabajando: Boolean = false,
    val mensaje: String? = null
)

/**
 * ViewModel del panel Admin.
 *
 * No conoce Room ni Retrofit: solo usa las funciones de ItemRepository.
 * Las cuentas salen de la base de datos local, por eso son los datos que la
 * persona realmente tiene guardados en el celular.
 */
class AdminViewModel(
    private val repo: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(AdminUiState())
    val estado: StateFlow<AdminUiState> = _estado.asStateFlow()

    init {
        viewModelScope.launch { leerTotales() }
    }

    /** Vuelve a leer el total guardado y el total de favoritos. */
    fun actualizarTotales() {
        viewModelScope.launch { leerTotales() }
    }

    /**
     * Descarga la lista desde la API y la guarda en Room.
     * Al terminar muestra cuántos elementos se guardaron (PA-10).
     */
    fun sincronizar() {
        if (_estado.value.trabajando) return
        viewModelScope.launch {
            _estado.update { it.copy(trabajando = true, mensaje = null) }
            repo.refrescar()
                .onSuccess { cantidad ->
                    _estado.update {
                        it.copy(
                            trabajando = false,
                            mensaje = "Se guardaron $cantidad elementos"
                        )
                    }
                    leerTotales()
                }
                .onFailure {
                    _estado.update {
                        it.copy(
                            trabajando = false,
                            mensaje = "No se pudo sincronizar. Revisa tu conexión a internet."
                        )
                    }
                }
        }
    }

    /**
     * Borra la caché local (PA-11). La lista y los totales quedan en cero;
     * al sincronizar vuelven los datos.
     */
    fun limpiarCache() {
        if (_estado.value.trabajando) return
        viewModelScope.launch {
            _estado.update { it.copy(trabajando = true, mensaje = null) }
            repo.limpiarCache()
            _estado.update {
                it.copy(trabajando = false, mensaje = "Caché local borrada")
            }
            leerTotales()
        }
    }

    private suspend fun leerTotales() {
        val guardados = repo.cantidadGuardada()
        val favoritos = repo.cantidadFavoritos()
        _estado.update {
            it.copy(
                cargando = false,
                totalGuardados = guardados,
                totalFavoritos = favoritos
            )
        }
    }
}

/** Factory que recibe el repositorio. */
class AdminViewModelFactory(
    private val repo: ItemRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            return AdminViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
