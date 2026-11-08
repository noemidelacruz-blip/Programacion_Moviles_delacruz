package com.delacruz.catalogo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.delacruz.catalogo.AppContainer
import com.delacruz.catalogo.domain.model.Rol
import com.delacruz.catalogo.domain.model.Sesion
import com.delacruz.catalogo.ui.admin.AdminScreen
import com.delacruz.catalogo.ui.admin.AdminViewModelFactory
import com.delacruz.catalogo.ui.detalle.DetalleScreen
import com.delacruz.catalogo.ui.detalle.DetalleViewModelFactory
import com.delacruz.catalogo.ui.favoritos.FavoritosScreen
import com.delacruz.catalogo.ui.favoritos.FavoritosViewModelFactory
import com.delacruz.catalogo.ui.lista.ListaScreen
import com.delacruz.catalogo.ui.lista.ListaViewModelFactory

/** Rutas de la aplicación (hito H14). */
private object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    const val FAVORITOS = "favoritos"
    const val ADMIN = "admin"

    fun detalle(id: Int) = "detalle/$id"
}

/**
 * Navegación de la app (hito H14).
 *
 * La sesión llega desde MainActivity y ya trae el rol leído de Firestore.
 * El rol decide qué se muestra: la acción "Panel Admin" solo aparece para
 * Rol.ADMIN y la ruta admin está protegida aquí, no en la pantalla.
 */
@Composable
fun AppNav(
    container: AppContainer,
    sesion: Sesion,
    onCerrarSesion: () -> Unit
) {
    val navController = rememberNavController()
    val esAdmin = sesion.rol == Rol.ADMIN

    NavHost(
        navController = navController,
        startDestination = Rutas.LISTA
    ) {
        composable(Rutas.LISTA) {
            ListaScreen(
                viewModel = viewModel(
                    factory = ListaViewModelFactory(
                        repo = container.itemRepository,
                        notificador = container.notificador
                    )
                ),
                esAdmin = esAdmin,
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) },
                onVerFavoritos = { navController.navigate(Rutas.FAVORITOS) },
                onVerAdmin = { navController.navigate(Rutas.ADMIN) },
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
                onVolver = { navController.popBackStack() },
                onItemClick = { id -> navController.navigate(Rutas.detalle(id)) }
            )
        }

        composable(Rutas.ADMIN) {
            if (esAdmin) {
                AdminScreen(
                    viewModel = viewModel(
                        factory = AdminViewModelFactory(container.itemRepository)
                    ),
                    onVolver = { navController.popBackStack() },
                    onCerrarSesion = onCerrarSesion
                )
            } else {
                // Protección extra: un usuario sin rol admin nunca ve el panel.
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }
    }
}
