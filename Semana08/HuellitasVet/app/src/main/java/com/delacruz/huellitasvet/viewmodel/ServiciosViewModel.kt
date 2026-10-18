package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel de la lista de servicios. Solo conoce el Repository (interface), no el DAO
class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    // Convierte el Flow de Room en StateFlow para que la pantalla lo observe.
    // WhileSubscribed(5000) mantiene la consulta viva 5 segundos si la pantalla se rota
    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Activa o desactiva un servicio (el switch de la lista)
    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    // TODO (Paso 13): guardar(servicio) y eliminar(servicio)
}