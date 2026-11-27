package com.delacruz.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.delacruz.avisoscampus.data.AvisosRepositoryMemoria
import com.delacruz.avisoscampus.ui.PantallaAvisos
import com.delacruz.avisoscampus.ui.theme.AvisosCampusTheme
import com.delacruz.avisoscampus.viewmodel.AvisosViewModel
import com.delacruz.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {

    // El ViewModel (y con él el historial) sobrevive al rotar la pantalla.
    private val vm: AvisosViewModel by viewModels {
        AvisosViewModelFactory(AvisosRepositoryMemoria())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvisosCampusTheme {
                PantallaAvisos(vm)
            }
        }
    }
}
