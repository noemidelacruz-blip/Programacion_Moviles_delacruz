package com.delacruz.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.campusgps.data.LugaresRepository
import com.delacruz.campusgps.data.UbicacionRepository
import com.delacruz.campusgps.model.Lugar
import com.delacruz.campusgps.model.LugarConDistancia
import com.delacruz.campusgps.model.Posicion
import com.delacruz.campusgps.model.TipoError
import com.delacruz.campusgps.model.UbicacionUi
import com.delacruz.campusgps.util.distanciaMetros
import com.delacruz.campusgps.util.formatearDistancia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    // Lista completa: se ordena por distancia cuando llega la ubicación
    private val _todos = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )

    // Categorías disponibles, en el orden en que aparecen en el repositorio
    // (Estudio, Comida, Eventos, Servicios)
    val categorias: List<String> = _todos.value.map { it.lugar.categoria }.distinct()

    // null = sin filtro; con una categoría solo se muestran esos lugares
    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    // Lo que ve la pantalla: el mapa y la lista usan esta misma lista filtrada
    val lugares: StateFlow<List<LugarConDistancia>> =
        combine(_todos, _categoria) { lista, filtro ->
            if (filtro == null) lista else lista.filter { it.lugar.categoria == filtro }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _todos.value)

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    // El primer lugar de la lista filtrada y ordenada que ya tiene distancia calculada
    val masCercano: StateFlow<Lugar?> = lugares
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun buscar() {
        // El ViewModel no recibe Context: le pregunta al Repository si hay Play Services
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services.",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }

        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = UbicacionUi.Error(mensajeDe(it), tipoDe(it)) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    // Un chip activo filtra el mapa y la lista; al tocarlo de nuevo se muestran todos
    fun filtrarPorCategoria(categoria: String?) {
        val filtro = if (_categoria.value == categoria) null else categoria
        _categoria.value = filtro

        // Si el lugar seleccionado deja de verse con el filtro, se limpia la selección
        val id = _seleccionado.value
        if (id != null && filtro != null &&
            _todos.value.none { it.lugar.id == id && it.lugar.categoria == filtro }
        ) {
            _seleccionado.value = null
        }
    }

    // Calcula la distancia a cada lugar y ordena la lista completa del más cercano al más lejano
    private fun actualizarDistancias(pos: Posicion) {
        _todos.value = _todos.value
            .map { LugarConDistancia(it.lugar, distanciaMetros(pos, it.lugar)) }
            .sortedBy { it.metros }

        Log.d(
            "CampusGPS",
            "Lugares ordenados: " + _todos.value.joinToString { "${it.lugar.nombre} (${formatearDistancia(it.metros ?: 0f)})" }
        )
    }

    private fun mensajeDe(e: Throwable): String = when (e) {
        is SecurityException -> "Sin permiso de ubicación no puedo mostrar dónde estás"
        // getCurrentLocation devuelve null (GPS apagado o sin ubicación) y el Repository
        // lo convierte en IllegalStateException
        is IllegalStateException -> "Activa el GPS e intenta de nuevo"
        else -> e.message ?: "No se pudo obtener la ubicación"
    }

    private fun tipoDe(e: Throwable): TipoError = when (e) {
        is SecurityException -> TipoError.SIN_PERMISO
        // getCurrentLocation devuelve null (GPS apagado o sin ubicación) y el Repository
        // lo convierte en IllegalStateException
        is IllegalStateException -> TipoError.GPS_APAGADO
        else -> TipoError.OTRO
    }
}
