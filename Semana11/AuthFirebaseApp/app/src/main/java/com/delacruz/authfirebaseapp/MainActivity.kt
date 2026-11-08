package com.delacruz.authfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.delacruz.authfirebaseapp.ui.theme.AuthFirebaseAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            // ¡AQUÍ ESTÁ LA CLAVE! Tiene que envolver la app con AuthFirebaseAppTheme
            AuthFirebaseAppTheme {
                AuthApp()
            }
        }
    }
}