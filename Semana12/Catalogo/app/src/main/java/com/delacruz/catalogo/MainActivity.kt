package com.delacruz.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.catalogo.ui.detalle.DetalleScreen
import com.delacruz.catalogo.ui.detalle.DetalleViewModelFactory
import com.delacruz.catalogo.ui.lista.ListaScreen
import com.delacruz.catalogo.ui.lista.ListaViewModelFactory
import com.delacruz.catalogo.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CatalogoApp).container

        setContent {
            CatalogoTheme {
                // Hito H7: cambio simple entre lista y detalle.
                // En H14 se reemplaza por la navegación (AppNav) con roles.
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                val id = idSeleccionado

                if (id == null) {
                    ListaScreen(
                        viewModel = viewModel(
                            factory = ListaViewModelFactory(container.itemRepository)
                        ),
                        onItemClick = { idSeleccionado = it }
                    )
                } else {
                    DetalleScreen(
                        viewModel = viewModel(
                            key = "detalle-$id",
                            factory = DetalleViewModelFactory(container.itemRepository, id)
                        ),
                        onVolver = { idSeleccionado = null }
                    )
                }
            }
        }
    }
}
