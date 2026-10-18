package com.delacruz.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.delacruz.huellitasvet.ui.navigation.NavVet

// Punto de entrada de la app: solo llama a NavVet, que decide el tema y la pantalla segun el perfil
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavVet()
        }
    }
}