package com.delacruz.centrotareas.model

//la carpeta model define los datos y la estruccion de los estados. la forma de los estados, o sea, qué datos se guardan y cómo se representan
sealed interface PrimosUi {
    data object Inicial : PrimosUi
    data object Calculando : PrimosUi
    data class Listo(val cantidad: Int, val milisegundos: Long) : PrimosUi
    data object Cancelado : PrimosUi
}

sealed interface CargaUi {
    data object Inicial : CargaUi
    data object Cargando : CargaUi
    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi
    data class Error(val mensaje: String) : CargaUi
}
