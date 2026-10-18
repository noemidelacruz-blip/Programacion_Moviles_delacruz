package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.delacruz.huellitasvet.data.prefs.PreferenciasStore
import com.delacruz.huellitasvet.data.repository.VetRepository

// Fabrica de ViewModels: los ViewModels reciben el repositorio (y las preferencias o el id del
// usuario, si lo necesitan) por constructor, y Android no sabe crearlos solo.
// Cada ViewModel nuevo se agrega como una rama mas del when
class VetViewModelFactory(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore,
    // Id del dueno con sesion iniciada, para los ViewModels que solo muestran sus datos
    private val usuarioId: Int = 0
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SesionViewModel::class.java) ->
                SesionViewModel(repo, prefs) as T
            modelClass.isAssignableFrom(ServiciosViewModel::class.java) ->
                ServiciosViewModel(repo) as T
            modelClass.isAssignableFrom(InicioViewModel::class.java) ->
                InicioViewModel(repo, usuarioId) as T
            modelClass.isAssignableFrom(MascotaViewModel::class.java) ->
                MascotaViewModel(repo, usuarioId) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}