package com.delacruz.campusgps

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.delacruz.campusgps.ui.PantallaMapa
import com.delacruz.campusgps.ui.theme.CampusGPSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La barra superior es azul, así que los íconos del sistema van en blanco
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            CampusGPSTheme {
                PantallaMapa()
            }
        }
    }
}
