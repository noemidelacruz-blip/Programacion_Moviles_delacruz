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
import com.delacruz.catalogo.ui.favoritos.FavoritosScreen
import com.delacruz.catalogo.ui.favoritos.FavoritosViewModelFactory
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
                // Hitos H7 y H10: cambio simple entre lista, detalle y favoritos.
                // En H14 se reemplaza por la navegación (AppNav) con roles.
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var verFavoritos by rememberSaveable { mutableStateOf(false) }

                val id = idSeleccionado
                when {
                    id != null -> DetalleScreen(
                        viewModel = viewModel(
                            key = "detalle-$id",
                            factory = DetalleViewModelFactory(container.itemRepository, id)
                        ),
                        onVolver = { idSeleccionado = null }
                    )

                    verFavoritos -> FavoritosScreen(
                        viewModel = viewModel(
                            factory = FavoritosViewModelFactory(container.itemRepository)
                        ),
                        onVolver = { verFavoritos = false },
                        onItemClick = { idSeleccionado = it }
                    )

                    else -> ListaScreen(
                        viewModel = viewModel(
                            factory = ListaViewModelFactory(container.itemRepository)
                        ),
                        onItemClick = { idSeleccionado = it },
                        onVerFavoritos = { verFavoritos = true }
                    )
                }
            }
        }
    }
}
