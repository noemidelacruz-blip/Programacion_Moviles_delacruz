package com.delacruz.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.delacruz.clinicasalud.navigation.AppNavigation
import com.delacruz.clinicasalud.ui.theme.ClinicaSaludTheme

/**
 * Actividad principal y punto de entrada de la aplicacion Android.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplicacion del tema visual global de Jetpack Compose
            ClinicaSaludTheme {
                // Contenedor principal de la interfaz
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Inicializacion del orquestador de navegacion
                    AppNavigation()
                }
            }
        }
    }
}