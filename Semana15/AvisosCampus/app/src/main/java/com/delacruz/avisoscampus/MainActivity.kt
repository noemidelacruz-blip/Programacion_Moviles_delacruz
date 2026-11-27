package com.delacruz.avisoscampus

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.datastore.preferences.preferencesDataStore
import com.delacruz.avisoscampus.data.AvisosRepositoryDataStore
import com.delacruz.avisoscampus.ui.PantallaAvisos
import com.delacruz.avisoscampus.ui.theme.AvisosCampusTheme
import com.delacruz.avisoscampus.viewmodel.AvisosViewModel
import com.delacruz.avisoscampus.viewmodel.AvisosViewModelFactory

private val Context.avisosDataStore by preferencesDataStore(name = "avisos")

class MainActivity : ComponentActivity() {

    // El ViewModel (y con él el historial) sobrevive al rotar la pantalla.
    private val vm: AvisosViewModel by viewModels {
        AvisosViewModelFactory(AvisosRepositoryDataStore(applicationContext.avisosDataStore))
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
