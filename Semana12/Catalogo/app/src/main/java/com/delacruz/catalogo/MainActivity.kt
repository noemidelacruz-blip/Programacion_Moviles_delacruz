package com.delacruz.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.catalogo.ui.lista.ListaScreen
import com.delacruz.catalogo.ui.lista.ListaViewModelFactory
import com.delacruz.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CatalogoApp).container
        val listaFactory = ListaViewModelFactory(container.itemRepository)

        setContent {
            CatalogoTheme {
                // Hito H6: por ahora se muestra la lista. En H14 se cambia por la navegación por rol.
                ListaScreen(
                    viewModel = viewModel(factory = listaFactory)
                )
            }
        }
    }
}
