package com.delacruz.tecsupstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.delacruz.tecsupstore.navigation.AppNavegacion
import com.delacruz.tecsupstore.ui.theme.TecsupStoreTheme


 //Punto de entrada de la aplicación TecsupStore.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TecsupStoreTheme {
                AppNavegacion()
            }
        }
    }
}