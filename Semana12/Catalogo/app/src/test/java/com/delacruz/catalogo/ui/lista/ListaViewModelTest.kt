package com.delacruz.catalogo.ui.lista

import com.delacruz.catalogo.data.repository.ItemRepository
import com.delacruz.catalogo.domain.model.Item
import com.delacruz.catalogo.notification.Notificador
import com.delacruz.catalogo.ui.UiState
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias de ListaViewModel.
 *
 * No usan Android, Room, Retrofit ni Firebase: el ViewModel depende de las
 * interfaces ItemRepository y Notificador, así que aquí se reemplazan por
 * dobles de prueba (FakeItemRepository y FakeNotificador).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun configurarDispatcherPrincipal() {
        // viewModelScope usa Dispatchers.Main; en pruebas se reemplaza por uno
        // de coroutines-test para poder controlar el avance del tiempo.
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun restaurarDispatcherPrincipal() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refrescar con exito deja la lista en Exito y muestra la notificacion`() =
        runTest(dispatcher.scheduler) {
            val productos = listOf(itemPrueba(1), itemPrueba(2))
            val repo = FakeItemRepository(
                guardados = productos,
                resultadoRefrescar = Result.success(productos.size)
            )
            val notificador = FakeNotificador()

            val viewModel = ListaViewModel(repo, notificador)
            advanceUntilIdle()

            assertEquals(UiState.Exito(productos), viewModel.estado.value)
            assertNull(viewModel.aviso.value)
            assertEquals(
                listOf("Datos actualizados" to "Se guardaron 2 elementos"),
                notificador.mensajes
            )
        }

    @Test
    fun `refrescar falla sin datos guardados deja el estado en Error`() =
        runTest(dispatcher.scheduler) {
            val repo = FakeItemRepository(
                guardados = emptyList(),
                resultadoRefrescar = Result.failure(IOException("sin red"))
            )
            val notificador = FakeNotificador()

            val viewModel = ListaViewModel(repo, notificador)
            advanceUntilIdle()

            assertEquals(
                UiState.Error("No hay conexión a internet"),
                viewModel.estado.value
            )
            assertNull(viewModel.aviso.value)
            assertTrue(notificador.mensajes.isEmpty())
        }

    @Test
    fun `refrescar falla con datos guardados deja Exito y aviso de sin conexion`() =
        runTest(dispatcher.scheduler) {
            val guardados = listOf(itemPrueba(1))
            val repo = FakeItemRepository(
                guardados = guardados,
                resultadoRefrescar = Result.failure(IOException("sin red"))
            )
            val notificador = FakeNotificador()

            val viewModel = ListaViewModel(repo, notificador)
            advanceUntilIdle()

            assertEquals(UiState.Exito(guardados), viewModel.estado.value)
            assertEquals(
                "Sin conexión: mostrando datos guardados",
                viewModel.aviso.value
            )
            assertTrue(notificador.mensajes.isEmpty())
        }
}

/**
 * Repositorio falso en memoria.
 *
 * Guarda la lista en un MutableStateFlow (igual que Room) y permite decidir
 * desde la prueba si refrescar() funciona o falla.
 */
private class FakeItemRepository(
    guardados: List<Item> = emptyList(),
    private val resultadoRefrescar: Result<Int> = Result.success(guardados.size)
) : ItemRepository {

    private val _items = MutableStateFlow(guardados)

    override val items: Flow<List<Item>> = _items.asStateFlow()

    override val favoritos: Flow<List<Item>> =
        _items.map { lista -> lista.filter { item -> item.esFavorito } }

    override suspend fun refrescar(): Result<Int> = resultadoRefrescar

    override suspend fun obtenerDetalle(id: Int): Result<Item> {
        val item = _items.value.firstOrNull { it.id == id }
        return if (item != null) {
            Result.success(item)
        } else {
            Result.failure(IllegalStateException("No existe el producto $id"))
        }
    }

    override suspend fun alternarFavorito(id: Int) {
        _items.value = _items.value.map { item ->
            if (item.id == id) item.copy(esFavorito = !item.esFavorito) else item
        }
    }

    override suspend fun cantidadGuardada(): Int = _items.value.size

    override suspend fun cantidadFavoritos(): Int = _items.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        _items.value = emptyList()
    }
}

/** Notificador falso que solo apunta los mensajes enviados. */
private class FakeNotificador : Notificador {

    val mensajes = mutableListOf<Pair<String, String>>()

    override fun mostrar(titulo: String, texto: String) {
        mensajes += titulo to texto
    }
}

/** Producto de prueba con todos los campos del modelo. */
private fun itemPrueba(id: Int, favorito: Boolean = false) = Item(
    id = id,
    titulo = "Labial de prueba $id",
    marca = "BeautyEmi",
    descripcion = "Producto de prueba",
    precio = 9.99,
    calificacion = 4.5,
    stock = 10,
    categoria = "beauty",
    imagenUrl = null,
    esFavorito = favorito
)
