package com.delacruz.saludpluscitas.data.model

// El correo es opcional en el registro: si no se llena queda como "".
data class Usuario(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val correo: String = "",
    val password: String
)