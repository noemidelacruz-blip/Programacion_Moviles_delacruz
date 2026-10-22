package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.relation.MascotaConDueno
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.Especie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// Chips de filtro por especie (especie = null significa sin filtro)
enum class FiltroPaciente(val etiqueta: String, val especie: Especie?) {
    TODOS("Todos", null),
    PERROS("Perros", Especie.PERRO),
    GATOS("Gatos", Especie.GATO),
    OTROS("Otros", Especie.OTRO)
}

// Un paciente de la lista: la mascota con su dueno y la fecha de su ultima visita (null = nunca)
data class PacienteItem(
    val mascota: MascotaConDueno,
    val ultimaVisita: String?
)

// ViewModel de Pacientes: lista de todas las mascotas filtrada por texto y por especie.
// Solo conoce el Repository (interface). El veterinario ve a todos los pacientes
class PacientesViewModel(repo: VetRepository) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _filtro = MutableStateFlow(FiltroPaciente.TODOS)
    val filtro: StateFlow<FiltroPaciente> = _filtro.asStateFlow()

    // combine junta las mascotas, las ultimas visitas, el texto y el filtro: la lista se recalcula
    // cuando cambia cualquiera de los cuatro
    val pacientes: StateFlow<List<PacienteItem>> = combine(
        repo.observarTodasLasMascotas(),
        repo.observarUltimasVisitas(),
        _busqueda,
        _filtro
    ) { mascotas, visitas, texto, filtro ->
        val fechaPorMascota = visitas.associate { it.mascotaId to it.fecha }
        val buscado = texto.trim()
        val especie = filtro.especie

        mascotas
            .filter { p ->
                // Filtro por especie (Todos no filtra)
                (especie == null || p.mascota.especie == especie.name) &&
                        // Texto: nombre de la mascota o del dueno
                        (buscado.isEmpty() ||
                                p.mascota.nombre.contains(buscado, ignoreCase = true) ||
                                p.dueno.nombre.contains(buscado, ignoreCase = true))
            }
            .map { p -> PacienteItem(p, fechaPorMascota[p.mascota.id]) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun elegirFiltro(filtro: FiltroPaciente) {
        _filtro.value = filtro
    }
}