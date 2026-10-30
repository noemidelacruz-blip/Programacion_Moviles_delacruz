package com.delacruz.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Persona que usa la app (tabla usuarios): puede ser dueno (CLIENTE), veterinario (ADMIN)
// o administrador (ADMINISTRADOR). apellidos y codigo solo se usan en el administrador.
// El rol se guarda como texto con Rol.name
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: String,
    val apellidos: String? = null,
    val codigo: String? = null
)