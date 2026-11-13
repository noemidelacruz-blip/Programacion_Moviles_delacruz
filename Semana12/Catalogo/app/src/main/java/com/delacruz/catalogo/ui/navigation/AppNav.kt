package com.delacruz.catalogo.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.delacruz.catalogo.AppContainer
import com.delacruz.catalogo.R
import com.delacruz.catalogo.domain.model.Rol
import com.delacruz.catalogo.domain.model.Sesion
import com.delacruz.catalogo.ui.admin.AdminScreen
import com.delacruz.catalogo.ui.admin.AdminViewModelFactory
import com.delacruz.catalogo.ui.components.FondoBeautyEmi
import com.delacruz.catalogo.ui.detalle.DetalleScreen
import com.delacruz.catalogo.ui.detalle.DetalleViewModelFactory
import com.delacruz.catalogo.ui.favoritos.FavoritosScreen
import com.delacruz.catalogo.ui.favoritos.FavoritosViewModelFactory
import com.delacruz.catalogo.ui.lista.ListaScreen
import com.delacruz.catalogo.ui.lista.ListaViewModelFactory
import com.delacruz.catalogo.ui.theme.Chocolate
import com.delacruz.catalogo.ui.theme.CreamSurface
import com.delacruz.catalogo.ui.theme.MutedBrown
import com.delacruz.catalogo.ui.theme.PeachSoft

/** Rutas de la aplicación (hito H14). */
private object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    const val FAVORITOS = "favoritos"
    const val ADMIN = "admin"

    fun detalle(id: Int) = "detalle/$id"
}

/**
 * Navegación de la app (hito H14) con barra inferior (rediseño visual).
 */
@Composable
fun AppNav(
    container: AppContainer,
    sesion: Sesion,
    onCerrarSesion: () -> Unit
) {
    val navController = rememberNavController()
    val esAdmin = sesion.rol == Rol.ADMIN

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val mostrarBarra = rutaActual == Rutas.LISTA ||
        rutaActual == Rutas.FAVORITOS ||
        rutaActual == Rutas.ADMIN

    FondoBeautyEmi {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (mostrarBarra) {
                    BarraNavegacion(
                        rutaActual = rutaActual,
                        esAdmin = esAdmin,
                        onIrACatalogo = { navController.navegarA(Rutas.LISTA) },
                        onIrAFavoritos = { navController.navegarA(Rutas.FAVORITOS) },
                        onIrAPanel = { navController.navegarA(Rutas.ADMIN) }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Rutas.LISTA,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Rutas.LISTA) {
                    ListaScreen(
                        viewModel = viewModel(
                            factory = ListaViewModelFactory(
                                repo = container.itemRepository,
                                notificador = container.notificador
                            )
                        ),
                        onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                        onAbrirFavoritos = { navController.navegarA(Rutas.FAVORITOS) },
                        onCerrarSesion = onCerrarSesion
                    )
                }

                composable(
                    route = Rutas.DETALLE,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entrada ->
                    val id = entrada.arguments?.getInt("id") ?: return@composable
                    DetalleScreen(
                        viewModel = viewModel(
                            factory = DetalleViewModelFactory(container.itemRepository, id)
                        ),
                        onVolver = { navController.popBackStack() }
                    )
                }

                composable(Rutas.FAVORITOS) {
                    FavoritosScreen(
                        viewModel = viewModel(
                            factory = FavoritosViewModelFactory(container.itemRepository)
                        ),
                        onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                        onCerrarSesion = onCerrarSesion
                    )
                }

                composable(Rutas.ADMIN) {
                    if (esAdmin) {
                        AdminScreen(
                            viewModel = viewModel(
                                factory = AdminViewModelFactory(container.itemRepository)
                            ),
                            onCerrarSesion = onCerrarSesion
                        )
                    } else {
                        // Protección extra: un usuario sin rol admin nunca ve el panel.
                        LaunchedEffect(Unit) { navController.popBackStack() }
                    }
                }
            }
        }
    }
}

/** Barra inferior redondeada con las pestañas de la app. */
@Composable
private fun BarraNavegacion(
    rutaActual: String?,
    esAdmin: Boolean,
    onIrACatalogo: () -> Unit,
    onIrAFavoritos: () -> Unit,
    onIrAPanel: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = CreamSurface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        NavigationBar(
            containerColor = CreamSurface,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            NavigationBarItem(
                selected = rutaActual == Rutas.LISTA,
                onClick = onIrACatalogo,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_catalogo),
                        contentDescription = "Catálogo"
                    )
                },
                label = { Etiqueta("Catálogo") },
                colors = coloresBarra()
            )

            NavigationBarItem(
                selected = rutaActual == Rutas.FAVORITOS,
                onClick = onIrAFavoritos,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_corazon_borde),
                        contentDescription = "Favoritos"
                    )
                },
                label = { Etiqueta("Favoritos") },
                colors = coloresBarra()
            )

            if (esAdmin) {
                NavigationBarItem(
                    selected = rutaActual == Rutas.ADMIN,
                    onClick = onIrAPanel,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_panel),
                            contentDescription = "Panel"
                        )
                    },
                    label = { Etiqueta("Panel") },
                    colors = coloresBarra()
                )
            }
        }
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun coloresBarra() = NavigationBarItemDefaults.colors(
    selectedIconColor = Chocolate,
    selectedTextColor = Chocolate,
    indicatorColor = PeachSoft,
    unselectedIconColor = MutedBrown,
    unselectedTextColor = MutedBrown
)

/** Navega entre pestañas conservando el estado de cada una. */
private fun NavHostController.navegarA(route: String) {
    navigate(route) {
        popUpTo(Rutas.LISTA) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
