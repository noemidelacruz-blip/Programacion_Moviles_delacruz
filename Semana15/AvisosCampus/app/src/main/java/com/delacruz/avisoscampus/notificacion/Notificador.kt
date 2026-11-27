package com.delacruz.avisoscampus.notificacion

import android.content.Context
import com.delacruz.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1: si SDK_INT >= O, crear NotificationChannel y registrarlo
    }

    // TODO Paso 2: fun tienePermiso(context: Context): Boolean

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3:
        // 1: si no tienePermiso(context) -> return false
        // 2: construir la notificación con NotificationCompat.Builder
        // 3: NotificationManagerCompat.from(context).notify(aviso.id, ...)
        // TODO Paso 4: abrir la app al tocar la notificación con un PendingIntent
        return true
    }
}
