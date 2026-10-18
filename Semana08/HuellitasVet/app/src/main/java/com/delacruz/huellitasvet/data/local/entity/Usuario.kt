package com.delacruz.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Persona que usa la app (tabla usuarios): puede ser dueno (CLIENTE) o veterinario (ADMIN).
// El rol se guarda como texto con Rol.name
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: String
)