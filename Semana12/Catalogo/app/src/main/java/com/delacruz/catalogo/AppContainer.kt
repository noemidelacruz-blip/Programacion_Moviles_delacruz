package com.delacruz.catalogo

import android.content.Context
import com.delacruz.catalogo.data.local.AppDatabase
import com.delacruz.catalogo.data.remote.RetrofitClient
import com.delacruz.catalogo.data.repository.ItemRepository
import com.delacruz.catalogo.data.repository.ItemRepositoryImpl

/**
 * Contenedor de dependencias de la aplicación.
 *
 * Aquí se crean UNA sola vez la API, la base de datos y los repositorios.
 * Los ViewModel los reciben mediante su Factory, así no los crean por dentro.
 */
class AppContainer(contexto: Context) {

    /** Base de datos Room (fuente única de verdad). */
    private val baseDatos by lazy {
        AppDatabase.obtener(contexto)
    }

    /** Repositorio de productos: Retrofit actualiza Room. */
    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(
            api = RetrofitClient.api,
            dao = baseDatos.itemDao()
        )
    }
}
