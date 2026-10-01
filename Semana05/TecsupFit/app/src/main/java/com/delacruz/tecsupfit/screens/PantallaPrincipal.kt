package com.delacruz.tecsupfit.screens

// Importaciones de Jetpack Compose y Material 3
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.delacruz.tecsupfit.model.ClaseFit

/**
 * Elemento de datos que define cada ítem de la barra de navegación inferior.
 */
data class ItemNavegacion(
    val titulo: String,
    val icono: ImageVector
)

/**
 * Pantalla contenedora principal que administra la navegación mediante BottomNavigationBar.
 * @param onClaseClick Callback para navegar al detalle de una clase.
 */
@Composable
fun PantallaPrincipal(
    onClaseClick: (ClaseFit) -> Unit
) {
    // Estado para rastrear el índice del ítem seleccionado en la barra inferior (0: Inicio, 1: Reservas, 2: Perfil)
    var indiceSeleccionado by remember { mutableIntStateOf(0) }

    // Lista de ítems disponibles para el menú inferior
    val items = listOf(
        ItemNavegacion("Inicio", Icons.Default.Home),
        ItemNavegacion("Reservas", Icons.Default.DateRange),
        ItemNavegacion("Perfil", Icons.Default.Person)
    )

    // Scaffold con la barra de navegación inferior configurada
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                items.forEachIndexed { indice, item ->
                    NavigationBarItem(
                        selected = indiceSeleccionado == indice,
                        onClick = { indiceSeleccionado = indice },
                        label = { Text(item.titulo) },
                        icon = {
                            Icon(
                                imageVector = item.icono,
                                contentDescription = item.titulo
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = VerdeTecsup,
                            selectedTextColor = VerdeTecsup,
                            indicatorColor = VerdeClaroBg
                        )
                    )
                }
            }
        }
    ) { paddingInterno ->
        // Renderizado condicional de la pantalla según el índice seleccionado
        Surface(modifier = Modifier.padding(paddingInterno)) {
            when (indiceSeleccionado) {
                0 -> PantallaInicio(onClaseClick = onClaseClick)
                1 -> PantallaReservas()
                2 -> PantallaPerfil()
            }
        }
    }
}