package com.example.libreria.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.libreria.Entities.UsuarioEntity


@Dao
interface UsuarioDao {

    @Query("SELECT * FROM UsuarioEntity where nombre = :nombre and contrasenya = :contrasenya")
    fun getUsuario(nombre: String?, contrasenya: String?): UsuarioEntity

    @Query("SELECT * FROM UsuarioEntity where nombre = :nombre ")
    fun comprobarUsuarioLogin(nombre: String?): UsuarioEntity

    @Query("INSERT INTO UsuarioEntity(nombre,contrasenya) values('usuario','usuario') ")
    fun insertUsuarioPred()

    @Query("DELETE FROM UsuarioEntity;")
    fun deleteUsuarios()

    @Insert
    fun addUsuario(usuarioEntity: UsuarioEntity)

    @Update
    fun updateUsuario(usuarioEntity: UsuarioEntity)

    @Delete
    fun deleteUsuario(usuarioEntity: UsuarioEntity)


}