package com.delacruz.catalogo.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.catalogo.domain.model.Item
import com.delacruz.catalogo.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de la lista (hito H4).
 *
 * Por ahora trabaja con datos de prueba para poder armar la pantalla sin
 * depender de internet. En el hito H6 se conecta al ItemRepository.
 *
 * La pantalla solo puede leer [estado]; nadie más lo modifica.
 */
class ListaViewModel : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        cargarDatosDePrueba()
    }

    /** Vuelve a mostrar el estado Cargando y los datos de prueba (botón Reintentar). */
    fun reintentar() {
        cargarDatosDePrueba()
    }

    private fun cargarDatosDePrueba() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            // Simula la demora de la red para poder ver el estado Cargando.
            delay(900)
            _estado.value = UiState.Exito(PRODUCTOS_DE_PRUEBA)
        }
    }

    private companion object {
        /** Productos beauty de DummyJSON usados como datos de prueba. */
        val PRODUCTOS_DE_PRUEBA = listOf(
            Item(
                id = 1,
                titulo = "Essence Mascara Lash Princess",
                marca = "Essence",
                descripcion = "Máscara de pestañas con acabado de volumen.",
                precio = 9.99,
                calificacion = 4.94,
                stock = 5,
                categoria = "beauty",
                imagenUrl = "https://cdn.dummyjson.com/products/images/beauty/Essence%20Mascara%20Lash%20Princess/thumbnail.png"
            ),
            Item(
                id = 2,
                titulo = "Eyeshadow Palette with Mirror",
                marca = "Glamour Beauty",
                descripcion = "Paleta de sombras con espejo incorporado.",
                precio = 19.99,
                calificacion = 3.28,
                stock = 44,
                categoria = "beauty",
                imagenUrl = "https://cdn.dummyjson.com/products/images/beauty/Eyeshadow%20Palette%20with%20Mirror/thumbnail.png"
            ),
            Item(
                id = 3,
                titulo = "Powder Canister",
                marca = "Velvet Touch",
                descripcion = "Polvo compacto de larga duración.",
                precio = 14.99,
                calificacion = 3.82,
                stock = 59,
                categoria = "beauty",
                imagenUrl = "https://cdn.dummyjson.com/products/images/beauty/Powder%20Canister/thumbnail.png"
            ),
            Item(
                id = 4,
                titulo = "Red Lipstick",
                marca = "Chic Cosmetics",
                descripcion = "Labial rojo de acabado mate.",
                precio = 12.99,
                calificacion = 4.5,
                stock = 90,
                categoria = "beauty",
                imagenUrl = "https://cdn.dummyjson.com/products/images/beauty/Red%20Lipstick/thumbnail.png"
            ),
            Item(
                id = 5,
                titulo = "Red Nail Polish",
                marca = "Nail Couture",
                descripcion = "Esmalte de uñas de secado rápido.",
                precio = 8.99,
                calificacion = 4.4,
                stock = 91,
                categoria = "beauty",
                imagenUrl = "https://cdn.dummyjson.com/products/images/beauty/Red%20Nail%20Polish/thumbnail.png"
            )
        )
    }
}
