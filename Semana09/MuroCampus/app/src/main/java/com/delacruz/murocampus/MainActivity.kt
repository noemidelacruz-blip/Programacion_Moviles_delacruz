package com.delacruz.murocampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.murocampus.data.PostRepositoryRemote
import com.delacruz.murocampus.data.remote.RetrofitClient
import com.delacruz.murocampus.ui.MuroCampusApp
import com.delacruz.murocampus.ui.theme.MuroCampusTheme
import com.delacruz.murocampus.viewmodel.PostsViewModel
import com.delacruz.murocampus.viewmodel.PostsViewModelFactory

/**
 * Punto de entrada: arma el Repository con Retrofit, crea el ViewModel con su
 * fábrica y entrega el control a la capa de UI. Toda la composición vive en
 * ui/MuroCampusApp.kt.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // El fondo es crema: iconos oscuros en las dos barras del sistema.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        val repository = PostRepositoryRemote(RetrofitClient.api)

        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(
                    factory = PostsViewModelFactory(repository)
                )
                MuroCampusApp(vm)
            }
        }
    }
}
