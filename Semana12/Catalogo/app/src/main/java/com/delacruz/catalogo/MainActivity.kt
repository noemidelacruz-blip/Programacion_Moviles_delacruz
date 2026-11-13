package com.delacruz.catalogo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.delacruz.catalogo.ui.CatalogoAppContent
import com.delacruz.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {

    // Registro del lanzador de permisos (debe hacerse antes de STARTED).
    // Si la persona niega el permiso, la app sigue funcionando (PA-14).
    private val lanzadorPermisoNotificaciones =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CatalogoApp).container

        // Solo la primera vez, no cada vez que se recrea la Activity (rotación).
        if (savedInstanceState == null) {
            pedirPermisoNotificaciones()
        }

        setContent {
            CatalogoTheme {
                CatalogoAppContent(container)
            }
        }
    }

    /** R09: en Android 13+ pide POST_NOTIFICATIONS si aún no está concedido. */
    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val concedido = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!concedido) {
            lanzadorPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}