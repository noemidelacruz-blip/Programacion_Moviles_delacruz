package com.delacruz.tecsupstore.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.delacruz.tecsupstore.components.TarjetaProducto
import com.delacruz.tecsupstore.model.Producto

/**
 * Pantalla principal del catálogo de productos.
 * Mapea la lista estática y delega las acciones de selección hacia el contenedor superior.
 */
@Composable
fun HomeScreen(
    favoritosIds: Set<Int>,
    onToggleFavorito: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listaProductos = listOf(
        Producto(1, "Audifonos", "S/ 89.00"),
        Producto(2, "Smartwatch", "S/ 199.00"),
        Producto(3, "Funda celular", "S/ 25.00")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        items(listaProductos) { producto ->
            TarjetaProducto(
                producto = producto,
                esFavorito = favoritosIds.contains(producto.id),
                onToggleFavorito = { onToggleFavorito(producto.id) }
            )
        }
    }
}