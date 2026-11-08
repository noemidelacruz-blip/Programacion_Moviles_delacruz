package com.delacruz.catalogo.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.delacruz.catalogo.MainActivity
import com.delacruz.catalogo.R

/**
 * Implementación de Notificador con NotificationCompat (hito H15).
 *
 * - Crea el canal de notificaciones al construir la clase.
 * - Si el permiso POST_NOTIFICATIONS está negado (PA-14), no muestra nada y
 *   la app sigue funcionando normal.
 * - Al tocar la notificación se abre la app (R09).
 */
class NotificadorAndroid(
    private val contexto: Context
) : Notificador {

    private val manager = NotificationManagerCompat.from(contexto)

    init {
        crearCanal()
    }

    override fun mostrar(titulo: String, texto: String) {
        // Desde Android 13 hay que tener el permiso concedido.
        if (!tienePermiso()) return

        val abrirApp = Intent(contexto, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            contexto,
            0,
            abrirApp,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(contexto, CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            manager.notify(ID_NOTIFICACION, notificacion)
        } catch (e: SecurityException) {
            // El permiso se revocó entre la comprobación y el envío.
        }
    }

    private fun tienePermiso(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun crearCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL,
                "Sincronización del catálogo",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Avisa cuántos productos se guardaron al sincronizar."
            }
            manager.createNotificationChannel(canal)
        }
    }

    companion object {
        /** Id del canal creado en la app. */
        const val CANAL = "sincronizacion"

        private const val ID_NOTIFICACION = 1
    }
}
