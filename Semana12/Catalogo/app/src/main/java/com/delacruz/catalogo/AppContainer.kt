package com.delacruz.catalogo

import android.content.Context
import com.delacruz.catalogo.data.auth.AuthRepository
import com.delacruz.catalogo.data.auth.FirebaseAuthRepository
import com.delacruz.catalogo.data.local.AppDatabase
import com.delacruz.catalogo.data.remote.RetrofitClient
import com.delacruz.catalogo.data.repository.ItemRepository
import com.delacruz.catalogo.data.repository.ItemRepositoryImpl

/**
 * Contenedor de dependencias de la aplicación.
 *
 * Aquí se crean UNA sola vez la API, la base de datos, los repositorios y el
 * repositorio de autenticación. Los ViewModel los reciben mediante su Factory,
 * así no los crean por dentro.
 */
class AppContainer(contexto: Context) {

    /** Base de datos Room (fuente única de verdad). */
    private val baseDatos by lazy {
        AppDatabase.obtener(contexto)
    }

    /** Autenticación con Firebase Authentication + rol desde Firestore. */
    val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository()
    }

    /** Repositorio de productos: Retrofit actualiza Room. */
    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(
            api = RetrofitClient.api,
            dao = baseDatos.itemDao()
        )
    }
}
