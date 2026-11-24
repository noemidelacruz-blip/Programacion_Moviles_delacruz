package com.delacruz.murocampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.murocampus.data.PostRepositoryRemote
import com.delacruz.murocampus.data.remote.RetrofitClient
import com.delacruz.murocampus.model.Post
import com.delacruz.murocampus.ui.PantallaLista
import com.delacruz.murocampus.ui.theme.MuroCampusTheme
import com.delacruz.murocampus.viewmodel.PostsViewModel
import com.delacruz.murocampus.viewmodel.PostsViewModelFactory
import com.delacruz.murocampus.viewmodel.UiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La app siempre usa fondo claro: iconos blancos sobre la barra azul y oscuros
        // sobre la barra de navegación inferior.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        val repository = PostRepositoryRemote(RetrofitClient.api)

        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(
                    factory = PostsViewModelFactory(repository)
                )
                val estado by vm.lista.collectAsStateWithLifecycle()

                MuroCampusApp(
                    estado = estado,
                    onReintentar = vm::cargar
                )
            }
        }
    }
}

/**
 * Contenedor principal de MuroCampus: barra superior y contenido.
 * La pantalla observa el estado del ViewModel y lo dibuja con un when.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuroCampusApp(
    estado: UiState<List<Post>>,
    onReintentar: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MuroCampus",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PantallaLista(
                estado = estado,
                onReintentar = onReintentar
            )
        }
    }
}
