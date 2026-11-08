package com.delacruz.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.delacruz.catalogo.ui.lista.ListaScreen
import com.delacruz.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogoTheme {
                // Hito H4: por ahora se muestra la lista. En H14 se cambia por la navegación por rol.
                ListaScreen()
            }
        }
    }
}
