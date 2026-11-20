package com.delacruz.campusgps.data

import com.delacruz.campusgps.model.Lugar

//Flujo completo: 1. LugaresRepositoryLocal.kt contiene los lugares y sus categorías. 2. MapaViewModel.kt obtiene esos lugares con obtenerLugares() y extrae las categorías. 3. PantallaMapa.kt recibe vm.categorias. 4. ChipsCategoria.kt muestra cada categoría como botón de filtro
class LugaresRepositoryLocal : LugaresRepository {

    override fun obtenerLugares(): List<Lugar> = listOf(
        Lugar(1, "Biblioteca Central", "Estudio", -12.0460, -77.0435),
        Lugar(2, "Cafetería Campus", "Comida", -12.0470, -77.0420),
        Lugar(3, "Laboratorio de Cómputo", "Estudio", -12.0455, -77.0415),
        Lugar(4, "Auditorio Principal", "Eventos", -12.0480, -77.0440),
        Lugar(5, "Estacionamiento Norte", "Servicios", -12.0440, -77.0430)
    )
}


