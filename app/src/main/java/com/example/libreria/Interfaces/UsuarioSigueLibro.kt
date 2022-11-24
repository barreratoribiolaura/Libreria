package com.example.libreria.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.libreria.Entities.LibroEntity


@Dao
interface UsuarioSigueLibro {

    @Query("SELECT * FROM LibroEntity inner join UsuarioSigueLibroEntity on UsuarioSigueLibroEntity.libro_id = LibroEntity.libro_id where UsuarioSigueLibroEntity.usuario_id = :usuario_id ;")
    fun getMiBiblioteca(usuario_id : Long): MutableList<LibroEntity>

    @Query("Insert into UsuarioSigueLibroEntity(usuario_id,libro_id) values(1,1),(1,3),(1,5);")
    fun insertUsuarioSiguePred()

    @Query("DELETE FROM UsuarioSigueLibroEntity;")
    fun deleteRelacionesPred()

    @Query("DELETE FROM UsuarioSigueLibroEntity where usuario_id = :usuario_id and libro_id = :libro_id;")
    fun deleteRelacion(usuario_id: Long,libro_id: Long)

    @Query("SELECT * from LibroEntity inner join UsuarioSigueLibroEntity on UsuarioSigueLibroEntity.libro_id = LibroEntity.libro_id where UsuarioSigueLibroEntity.libro_id = :libro_id and UsuarioSigueLibroEntity.usuario_id = :usuario_id")
    fun getLibro(usuario_id: Long,libro_id: Long) : LibroEntity

    @Query("Select exists(select * from UsuarioSigueLibroEntity where usuario_id = :usuario_id and libro_id = :libro_id) ;")
    fun comprobarFavorito(usuario_id: Long?, libro_id: Long) : Boolean

    @Insert()
    fun addLibro(libroEntity: LibroEntity)

    @Update
    fun updateLibro(libroEntity: LibroEntity)

    @Delete
    fun deleteLibro(libroEntity: LibroEntity)

    @Query("DELETE FROM UsuarioSigueLibroEntity where usuario_id = :usuario_id and libro_id = :libro_id;")
    fun dejardeSeguir(libro_id : Long, usuario_id : Long)

    @Query("Insert into UsuarioSigueLibroEntity(usuario_id,libro_id) values(:usuarioId,:libroId);")
    fun seguirLibro(libroId: Long, usuarioId: Long)


}