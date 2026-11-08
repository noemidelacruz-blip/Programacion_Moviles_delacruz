package com.delacruz.catalogo

import com.delacruz.catalogo.data.remote.RetrofitClient
import com.delacruz.catalogo.data.repository.ItemRepository
import com.delacruz.catalogo.data.repository.ItemRepositoryImpl

/**
 * Contenedor de dependencias de la aplicación.
 *
 * Aquí se crean UNA sola vez la API, la base de datos y los repositorios.
 * Los ViewModel los reciben mediante su Factory, así no los crean por dentro.
 */
class AppContainer {

    /** Repositorio de productos: implementación real que usa Retrofit. */
    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(api = RetrofitClient.api)
    }
}
