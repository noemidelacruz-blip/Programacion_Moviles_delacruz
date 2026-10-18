package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.delacruz.huellitasvet.data.prefs.PreferenciasStore
import com.delacruz.huellitasvet.data.repository.VetRepository

// Fabrica de ViewModels: los ViewModels reciben el repositorio (y las preferencias, si las
// necesitan) por constructor, y Android no sabe crearlos solo, por eso se necesita esta clase.
// Cada ViewModel nuevo se agrega como una rama mas del when
class VetViewModelFactory(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SesionViewModel::class.java) ->
                SesionViewModel(repo, prefs) as T
            modelClass.isAssignableFrom(ServiciosViewModel::class.java) ->
                ServiciosViewModel(repo) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}