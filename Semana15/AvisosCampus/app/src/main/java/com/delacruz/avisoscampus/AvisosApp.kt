package com.delacruz.avisoscampus

import android.app.Application
import com.delacruz.avisoscampus.notificacion.Notificador

class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}
