package com.delacruz.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.delacruz.huellitasvet.data.local.entity.Usuario

@Dao
interface UsuarioDao {

    // Busca un usuario por correo y clave. Devuelve null si las credenciales no coinciden
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    // Inserta varios usuarios y devuelve sus ids (los datos iniciales los usan para crear mascotas)
    @Insert
    suspend fun insertarTodos(lista: List<Usuario>): List<Long>

    // Cuantos usuarios hay (se usa para sembrar los datos iniciales solo una vez)
    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}