package com.delacruz.authfirebaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            // Llamamos directamente a LoginScreen sin usar AuthApp()
            LoginScreen(
                onNavigateToRegister = { },
                onLoginSuccess = { }
            )
        }
    }
}