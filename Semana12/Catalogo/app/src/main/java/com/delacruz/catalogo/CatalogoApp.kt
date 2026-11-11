package com.delacruz.catalogo

import android.app.Application

/**
 * Application de la app: crea el AppContainer una sola vez y lo deja
 * disponible para las pantallas mientras la app esté viva.
 */
class CatalogoApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
