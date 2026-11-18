package com.delacruz.centrotareas

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.delacruz.centrotareas.data.TareasRepositoryLocal
import com.delacruz.centrotareas.ui.PantallaTareas
import com.delacruz.centrotareas.ui.theme.CentroTareasTheme
import com.delacruz.centrotareas.viewmodel.TareasViewModel
import com.delacruz.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContent {
            CentroTareasTheme {
                val vm: TareasViewModel = viewModel(
                    factory = TareasViewModelFactory(TareasRepositoryLocal(applicationContext))
                )
                PantallaTareas(vm)
            }
        }
    }
}
